package com.eyenet.sdn;

import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.document.NetworkDeviceDocument;
import com.eyenet.mapper.FlowRuleMapper;
import com.eyenet.mapper.NetworkDeviceMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.AttributeKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OpenFlowMessageHandlerTest {

    private OpenFlowMessageHandler handler;
    private EmbeddedChannel channel;

    @Mock
    private NetworkDeviceDocument mockDevice;
    
    @Mock
    private FlowRuleDocument mockFlowRule;
    
    @Mock
    private FlowRuleMapper flowRuleMapper;
    
    @Mock
    private NetworkDeviceMapper networkDeviceMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup mock device
        String deviceId = UUID.randomUUID().toString();
        when(mockDevice.getId()).thenReturn(deviceId);
        
        // Setup mock flow rule
        when(mockFlowRule.getTableId()).thenReturn(0);
        when(mockFlowRule.getPriority()).thenReturn(100);
        when(mockFlowRule.getTimeoutIdle()).thenReturn(30);
        when(mockFlowRule.getTimeoutHard()).thenReturn(300);
        when(mockFlowRule.getCookie()).thenReturn(123L);
        
        // Setup mock mappers
        when(flowRuleMapper.toDocument(any())).thenReturn(FlowRuleDocument.builder()
                .id(UUID.randomUUID().toString())
                .deviceId(deviceId)
                .tableId(0)
                .priority(100)
                .timeoutIdle(30)
                .timeoutHard(300)
                .cookie(123L)
                .build());
        
        handler = new OpenFlowMessageHandler(flowRuleMapper, networkDeviceMapper);
        channel = new EmbeddedChannel(handler);
        channel.attr(AttributeKey.valueOf("deviceId")).set(deviceId);
    }

    @Test
    void testChannelActive() {
        // Verify that HELLO message is sent when channel becomes active
        Object msg = channel.readOutbound();
        assertTrue(msg instanceof OpenFlowMessage);
        OpenFlowMessage hello = (OpenFlowMessage) msg;
        
        assertEquals(0x04, hello.getVersion()); // version
        assertEquals(OpenFlowMessage.Type.HELLO, hello.getType()); // type
        assertEquals(8, hello.getLength()); // length
        assertEquals(0, hello.getXid()); // xid
        
        hello.release();
        
        // Verify channel is active
        assertTrue(channel.isActive());
    }

    @Test
    void testChannelInactive() {
        channel.finish();
        assertFalse(channel.isActive());
    }

    @Test
    void testHandleHello() {
        // Create and send HELLO message
        ByteBuf helloBuf = Unpooled.buffer();
        try {
            helloBuf.writeByte(0x04);
            helloBuf.writeByte(OpenFlowMessage.Type.HELLO);
            helloBuf.writeShort(8);
            helloBuf.writeInt(0);
            
            OpenFlowMessage helloMsg = new OpenFlowMessage((byte)0x04, OpenFlowMessage.Type.HELLO, (short)8, 0, helloBuf);
            channel.writeInbound(helloMsg);

            // Verify FEATURES_REQUEST is sent in response
            Object msg = channel.readOutbound();
            assertTrue(msg instanceof OpenFlowMessage);
            OpenFlowMessage featuresRequest = (OpenFlowMessage) msg;
            
            assertEquals(0x04, featuresRequest.getVersion()); // version
            assertEquals(OpenFlowMessage.Type.FEATURES_REQUEST, featuresRequest.getType()); // type
            assertEquals(8, featuresRequest.getLength()); // length
            assertEquals(1, featuresRequest.getXid()); // xid
            
            featuresRequest.release();
        } finally {
            helloBuf.release();
        }
    }

    @Test
    void testHandleFeaturesRequest() {
        // Create and send FEATURES_REQUEST message
        ByteBuf featuresBuf = Unpooled.buffer();
        try {
            featuresBuf.writeByte(0x04);
            featuresBuf.writeByte(OpenFlowMessage.Type.FEATURES_REQUEST);
            featuresBuf.writeShort(8);
            featuresBuf.writeInt(123); // specific xid
            
            OpenFlowMessage featuresMsg = new OpenFlowMessage((byte)0x04, OpenFlowMessage.Type.FEATURES_REQUEST, (short)8, 123, featuresBuf);
            channel.writeInbound(featuresMsg);

            // Verify FEATURES_REPLY is sent with correct format
            Object msg = channel.readOutbound();
            assertTrue(msg instanceof OpenFlowMessage);
            OpenFlowMessage reply = (OpenFlowMessage) msg;
            
            assertEquals(0x04, reply.getVersion()); // version
            assertEquals(OpenFlowMessage.Type.FEATURES_REPLY, reply.getType()); // type
            assertTrue(reply.getLength() > 8); // Should be longer than header
            assertEquals(123, reply.getXid()); // Should match request xid
            
            ByteBuf payload = reply.getPayload();
            assertEquals(0L, payload.readLong()); // datapath_id
            assertEquals(0, payload.readInt()); // n_buffers
            assertEquals(0, payload.readByte()); // n_tables
            assertEquals(0, payload.readByte()); // auxiliary_id
            assertEquals(0, payload.readShort()); // pad
            assertEquals(0, payload.readInt()); // capabilities
            assertEquals(0, payload.readInt()); // reserved
            
            reply.release();
        } finally {
            featuresBuf.release();
        }
    }

    @Test
    void testHandlePacketIn() {
        // Create a PACKET_IN message with test data
        ByteBuf packetBuf = Unpooled.buffer();
        try {
            packetBuf.writeByte(0x04);
            packetBuf.writeByte(OpenFlowMessage.Type.PACKET_IN);
            packetBuf.writeShort(32); // length
            packetBuf.writeInt(456); // xid
            
            // Add packet metadata
            packetBuf.writeInt(789); // buffer_id
            packetBuf.writeShort(20); // total_len
            packetBuf.writeByte(0); // reason (OFPR_NO_MATCH)
            packetBuf.writeByte(0); // table_id
            packetBuf.writeLong(0L); // cookie
            
            // Add some dummy packet data
            packetBuf.writeBytes(new byte[10]);
            
            OpenFlowMessage packetInMsg = new OpenFlowMessage((byte)0x04, OpenFlowMessage.Type.PACKET_IN, (short)32, 456, packetBuf);
            channel.writeInbound(packetInMsg);
            
            // Verify channel is still active after processing
            assertTrue(channel.isActive());
            
            // Verify buffer reference count
            assertEquals(0, packetBuf.refCnt());
        } finally {
            if (packetBuf.refCnt() > 0) {
                packetBuf.release();
            }
        }
    }

    @Test
    void testSendFlowMod() {
        // Test sending flow modification
        handler.sendFlowMod(mockDevice, mockFlowRule);
        
        // Verify a message was sent
        Object msg = channel.readOutbound();
        assertTrue(msg instanceof OpenFlowMessage);
        OpenFlowMessage flowMod = (OpenFlowMessage) msg;
        
        assertEquals(0x04, flowMod.getVersion()); // version
        assertEquals(OpenFlowMessage.Type.FLOW_MOD, flowMod.getType()); // type
        assertTrue(flowMod.getLength() >= 56); // Minimum flow mod message size
        
        ByteBuf payload = flowMod.getPayload();
        assertEquals(123L, payload.readLong()); // cookie
        assertEquals(0L, payload.readLong()); // cookie_mask
        assertEquals(0, payload.readByte()); // table_id
        assertEquals(0, payload.readByte()); // command (OFPFC_ADD)
        assertEquals(30, payload.readShort()); // idle_timeout
        assertEquals(300, payload.readShort()); // hard_timeout
        assertEquals(100, payload.readShort()); // priority
        assertEquals(-1, payload.readInt()); // buffer_id (NONE)
        assertEquals(-1, payload.readInt()); // out_port (ANY)
        assertEquals(-1, payload.readInt()); // out_group (ANY)
        assertEquals(0, payload.readShort()); // flags
        assertEquals(0, payload.readShort()); // pad
        
        flowMod.release();
    }

    @Test
    void testRemoveFlowMod() {
        // Test removing flow rule
        handler.removeFlowMod(mockDevice, mockFlowRule);
        
        // Verify a message was sent
        Object msg = channel.readOutbound();
        assertTrue(msg instanceof OpenFlowMessage);
        OpenFlowMessage flowMod = (OpenFlowMessage) msg;
        
        assertEquals(0x04, flowMod.getVersion()); // version
        assertEquals(OpenFlowMessage.Type.FLOW_MOD, flowMod.getType()); // type
        assertTrue(flowMod.getLength() >= 56); // Minimum flow mod message size
        
        ByteBuf payload = flowMod.getPayload();
        assertEquals(123L, payload.readLong()); // cookie
        assertEquals(0L, payload.readLong()); // cookie_mask
        assertEquals(0, payload.readByte()); // table_id
        assertEquals(3, payload.readByte()); // command (OFPFC_DELETE)
        assertEquals(30, payload.readShort()); // idle_timeout
        assertEquals(300, payload.readShort()); // hard_timeout
        assertEquals(100, payload.readShort()); // priority
        assertEquals(-1, payload.readInt()); // buffer_id (NONE)
        assertEquals(-1, payload.readInt()); // out_port (ANY)
        assertEquals(-1, payload.readInt()); // out_group (ANY)
        assertEquals(0, payload.readShort()); // flags
        assertEquals(0, payload.readShort()); // pad
        
        flowMod.release();
    }

    @Test
    void testUpdateFlowMod() {
        // Test updating flow rule
        handler.updateFlowMod(mockDevice, mockFlowRule);
        
        // Verify a message was sent
        Object msg = channel.readOutbound();
        assertTrue(msg instanceof OpenFlowMessage);
        OpenFlowMessage flowMod = (OpenFlowMessage) msg;
        
        assertEquals(0x04, flowMod.getVersion()); // version
        assertEquals(OpenFlowMessage.Type.FLOW_MOD, flowMod.getType()); // type
        assertTrue(flowMod.getLength() >= 56); // Minimum flow mod message size
        
        ByteBuf payload = flowMod.getPayload();
        assertEquals(123L, payload.readLong()); // cookie
        assertEquals(0L, payload.readLong()); // cookie_mask
        assertEquals(0, payload.readByte()); // table_id
        assertEquals(1, payload.readByte()); // command (OFPFC_MODIFY)
        assertEquals(30, payload.readShort()); // idle_timeout
        assertEquals(300, payload.readShort()); // hard_timeout
        assertEquals(100, payload.readShort()); // priority
        assertEquals(-1, payload.readInt()); // buffer_id (NONE)
        assertEquals(-1, payload.readInt()); // out_port (ANY)
        assertEquals(-1, payload.readInt()); // out_group (ANY)
        assertEquals(0, payload.readShort()); // flags
        assertEquals(0, payload.readShort()); // pad
        
        flowMod.release();
    }
}
