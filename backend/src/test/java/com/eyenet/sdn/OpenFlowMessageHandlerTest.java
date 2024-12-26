package com.eyenet.sdn;

import com.eyenet.model.entity.FlowRule;
import com.eyenet.model.entity.NetworkDevice;
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
    private Map<ChannelId, Channel> switchConnections;
    private EmbeddedChannel channel;

    @Mock
    private NetworkDevice mockDevice;
    
    @Mock
    private FlowRule mockFlowRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        switchConnections = new HashMap<>();
        handler = new OpenFlowMessageHandler(switchConnections);
        channel = new EmbeddedChannel(handler);
        
        // Setup mock device
        when(mockDevice.getId()).thenReturn(UUID.randomUUID());
        channel.attr(AttributeKey.valueOf("deviceId")).set(mockDevice.getId());
        switchConnections.put(channel.id(), channel);
    }

    @Test
    void testChannelActive() {
        // Verify that HELLO message is sent when channel becomes active
        ByteBuf writtenBuffer = channel.readOutbound();
        assertNotNull(writtenBuffer);
        assertEquals(0x04, writtenBuffer.readByte()); // version
        assertEquals(OpenFlowMessage.Type.HELLO, writtenBuffer.readByte()); // type
        assertEquals(8, writtenBuffer.readShort()); // length
        assertEquals(0, writtenBuffer.readInt()); // xid
        writtenBuffer.release();
        
        // Verify channel is added to connections
        assertTrue(switchConnections.containsKey(channel.id()));
    }

    @Test
    void testChannelInactive() {
        channel.finish();
        assertFalse(switchConnections.containsKey(channel.id()));
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
            ByteBuf writtenBuffer = channel.readOutbound();
            assertNotNull(writtenBuffer);
            try {
                assertEquals(0x04, writtenBuffer.readByte()); // version
                assertEquals(OpenFlowMessage.Type.FEATURES_REQUEST, writtenBuffer.readByte()); // type
                assertEquals(8, writtenBuffer.readShort()); // length
                assertEquals(1, writtenBuffer.readInt()); // xid
            } finally {
                writtenBuffer.release();
            }
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
            ByteBuf writtenBuffer = channel.readOutbound();
            assertNotNull(writtenBuffer);
            try {
                assertEquals(0x04, writtenBuffer.readByte()); // version
                assertEquals(OpenFlowMessage.Type.FEATURES_REPLY, writtenBuffer.readByte()); // type
                int length = writtenBuffer.readShort();
                assertTrue(length > 8); // Should be longer than header
                assertEquals(123, writtenBuffer.readInt()); // Should match request xid
                
                // Verify switch capabilities
                assertEquals(0L, writtenBuffer.readLong()); // datapath_id
                assertEquals(0, writtenBuffer.readInt()); // n_buffers
                assertEquals(0, writtenBuffer.readByte()); // n_tables
                assertEquals(0, writtenBuffer.readByte()); // auxiliary_id
                assertEquals(0, writtenBuffer.readShort()); // pad
                assertEquals(0, writtenBuffer.readInt()); // capabilities
                assertEquals(0, writtenBuffer.readInt()); // reserved
            } finally {
                writtenBuffer.release();
            }
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
        ByteBuf writtenBuffer = channel.readOutbound();
        assertNotNull(writtenBuffer);
        try {
            assertEquals(0x04, writtenBuffer.readByte()); // version
            assertEquals(OpenFlowMessage.Type.FLOW_MOD, writtenBuffer.readByte()); // type
            int length = writtenBuffer.readShort();
            assertTrue(length >= 56); // Minimum flow mod message size
            
            // Skip xid as it's generated
            writtenBuffer.skipBytes(4);
            
            // Verify flow mod fields
            assertEquals(0L, writtenBuffer.readLong()); // cookie
            assertEquals(0L, writtenBuffer.readLong()); // cookie_mask
            assertEquals(0, writtenBuffer.readByte()); // table_id
            assertEquals(0, writtenBuffer.readByte()); // command (OFPFC_ADD)
            assertEquals(0, writtenBuffer.readShort()); // idle_timeout
            assertEquals(0, writtenBuffer.readShort()); // hard_timeout
            assertEquals(0, writtenBuffer.readShort()); // priority
            assertEquals(-1, writtenBuffer.readInt()); // buffer_id (NONE)
            assertEquals(-1, writtenBuffer.readInt()); // out_port (ANY)
            assertEquals(-1, writtenBuffer.readInt()); // out_group (ANY)
            assertEquals(0, writtenBuffer.readShort()); // flags
            assertEquals(0, writtenBuffer.readShort()); // pad
        } finally {
            writtenBuffer.release();
        }
    }

    @Test
    void testSendFlowModWithDisconnectedDevice() {
        // Create a new device that isn't connected
        NetworkDevice disconnectedDevice = mock(NetworkDevice.class);
        when(disconnectedDevice.getId()).thenReturn(UUID.randomUUID());
        
        // Try to send flow mod to disconnected device
        handler.sendFlowMod(disconnectedDevice, mockFlowRule);
        
        // Verify no message was sent
        ByteBuf writtenBuffer = channel.readOutbound();
        assertNull(writtenBuffer);
    }

    @Test
    void testResourceCleanup() {
        // Create a message that will cause an error
        ByteBuf invalidBuf = Unpooled.buffer();
        try {
            invalidBuf.writeByte(0x04);
            invalidBuf.writeByte(99); // Invalid message type
            invalidBuf.writeShort(8);
            invalidBuf.writeInt(0);
            
            OpenFlowMessage invalidMsg = new OpenFlowMessage((byte)0x04, (byte)99, (short)8, 0, invalidBuf);
            channel.writeInbound(invalidMsg);
            
            // Verify channel is still active and buffer is released
            assertTrue(channel.isActive());
            assertEquals(0, invalidBuf.refCnt());
        } finally {
            if (invalidBuf.refCnt() > 0) {
                invalidBuf.release();
            }
        }
    }
}
