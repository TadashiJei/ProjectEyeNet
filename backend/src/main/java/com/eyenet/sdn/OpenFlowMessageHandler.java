package com.eyenet.sdn;

import com.eyenet.mapper.FlowRuleMapper;
import com.eyenet.mapper.NetworkDeviceMapper;
import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.entity.NetworkDevice;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class OpenFlowMessageHandler extends SimpleChannelInboundHandler<OpenFlowMessage> {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenFlowMessageHandler.class);
    private static final AttributeKey<UUID> DEVICE_ID_KEY = AttributeKey.valueOf("deviceId");
    
    private final Map<ChannelId, Channel> switchConnections = new ConcurrentHashMap<>();
    private final FlowRuleMapper flowRuleMapper;
    private final NetworkDeviceMapper networkDeviceMapper;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("New switch connected from: {}", ctx.channel().remoteAddress());
        switchConnections.put(ctx.channel().id(), ctx.channel());
        
        // Send HELLO message
        ByteBuf buf = ctx.alloc().buffer(8);
        buf.writeByte(0x04); // version
        buf.writeByte(OpenFlowMessage.Type.HELLO); // type
        buf.writeShort(8); // length
        buf.writeInt(0); // xid
        
        ctx.writeAndFlush(new OpenFlowMessage(
            (byte) 0x04,
            OpenFlowMessage.Type.HELLO,
            (short) 8,
            0,
            buf
        ));
        logger.debug("Sent HELLO to switch: {}", ctx.channel().remoteAddress());
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("Switch disconnected: {}", ctx.channel().remoteAddress());
        switchConnections.remove(ctx.channel().id());
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, OpenFlowMessage msg) {
        if (msg == null || msg.getPayload() == null || !msg.getPayload().isReadable()) {
            logger.error("Received null or unreadable message");
            return;
        }

        try {
            switch (msg.getType()) {
                case OpenFlowMessage.Type.HELLO:
                    handleHello(ctx, msg);
                    break;
                case OpenFlowMessage.Type.FEATURES_REQUEST:
                    handleFeaturesRequest(ctx, msg);
                    break;
                case OpenFlowMessage.Type.PACKET_IN:
                    handlePacketIn(ctx, msg);
                    break;
                default:
                    logger.debug("Unhandled message type: {}", msg.getType());
            }
        } catch (Exception e) {
            logger.error("Error processing message type {}", msg.getType(), e);
        } finally {
            msg.release();
        }
    }
    
    private void handleHello(ChannelHandlerContext ctx, OpenFlowMessage msg) {
        logger.info("Received HELLO from switch: {}", ctx.channel().remoteAddress());
        
        // Send Features Request
        ByteBuf buf = ctx.alloc().buffer(8);
        buf.writeByte(0x04); // version
        buf.writeByte(OpenFlowMessage.Type.FEATURES_REQUEST); // type
        buf.writeShort(8); // length
        buf.writeInt(1); // xid
        
        ctx.writeAndFlush(new OpenFlowMessage(
            (byte) 0x04,
            OpenFlowMessage.Type.FEATURES_REQUEST,
            (short) 8,
            1,
            buf
        ));
        logger.debug("Sent FEATURES_REQUEST to switch: {}", ctx.channel().remoteAddress());
    }
    
    private void handleFeaturesRequest(ChannelHandlerContext ctx, OpenFlowMessage msg) {
        logger.info("Received FEATURES_REQUEST from switch: {}", ctx.channel().remoteAddress());
        
        // Build features reply
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeByte(0x04); // version
        buf.writeByte(OpenFlowMessage.Type.FEATURES_REPLY);
        buf.writeShort(32); // length
        buf.writeInt(msg.getXid()); // Same transaction id as request
        buf.writeLong(0L); // Datapath ID
        buf.writeInt(0); // n_buffers
        buf.writeByte(0); // n_tables
        buf.writeByte(0); // auxiliary_id
        buf.writeShort(0); // pad
        buf.writeInt(0); // capabilities
        buf.writeInt(0); // reserved
        
        ctx.writeAndFlush(new OpenFlowMessage(
            (byte) 0x04,
            OpenFlowMessage.Type.FEATURES_REPLY,
            (short) 32,
            msg.getXid(),
            buf
        ));
        logger.debug("Sent FEATURES_REPLY to switch: {}", ctx.channel().remoteAddress());
    }
    
    private void handlePacketIn(ChannelHandlerContext ctx, OpenFlowMessage msg) {
        logger.info("Received PACKET_IN from switch: {}", ctx.channel().remoteAddress());
        ByteBuf payload = msg.getPayload();
        
        try {
            if (payload.readableBytes() < 24) {
                logger.error("Packet-in message too short");
                return;
            }

            int bufferId = payload.readInt();
            int totalLen = payload.readShort() & 0xFFFF;
            byte reason = payload.readByte();
            byte tableId = payload.readByte();
            long cookie = payload.readLong();
            
            switch (reason) {
                case 0:
                    handleNoMatchPacket(ctx, payload, bufferId);
                    break;
                case 1:
                    handleActionPacket(ctx, payload, bufferId);
                    break;
                default:
                    logger.warn("Unknown packet-in reason: {}", reason);
            }
        } catch (Exception e) {
            logger.error("Error processing PACKET_IN message", e);
        }
    }

    private void handleNoMatchPacket(ChannelHandlerContext ctx, ByteBuf payload, int bufferId) {
        logger.debug("Processing no-match packet with buffer ID: {}", bufferId);
    }

    private void handleActionPacket(ChannelHandlerContext ctx, ByteBuf payload, int bufferId) {
        logger.debug("Processing action packet with buffer ID: {}", bufferId);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Error handling OpenFlow message", cause);
        ctx.close();
    }

    public void sendFlowMod(NetworkDevice device, FlowRuleDocument flowRule) {
        Channel channel = getChannelForDevice(device);
        if (channel != null && channel.isActive()) {
            ByteBuf buf = Unpooled.buffer();
            
            buf.writeLong(flowRule.getCookie());
            buf.writeLong(0L);
            buf.writeByte(flowRule.getTableId());
            buf.writeByte(0);
            buf.writeShort(flowRule.getIdleTimeout());
            buf.writeShort(flowRule.getHardTimeout());
            buf.writeShort(flowRule.getPriority());
            buf.writeInt(-1);
            buf.writeInt(-1);
            buf.writeInt(-1);
            buf.writeShort(0);
            buf.writeShort(0);
            
            writeMatchFields(buf, flowRule);
            writeInstructions(buf, flowRule);
            
            channel.writeAndFlush(new OpenFlowMessage(
                (byte) 0x04,
                OpenFlowMessage.Type.FLOW_MOD,
                (short) buf.readableBytes(),
                generateXid(),
                buf
            ));
            logger.info("Sent flow mod message to device: {}", device.getId());
        } else {
            logger.error("Device {} is not connected", device.getId());
        }
    }

    public void removeFlowMod(NetworkDevice device, FlowRuleDocument flowRule) {
        Channel channel = getChannelForDevice(device);
        if (channel != null && channel.isActive()) {
            ByteBuf buf = Unpooled.buffer();
            
            buf.writeLong(flowRule.getCookie());
            buf.writeLong(0L);
            buf.writeByte(flowRule.getTableId());
            buf.writeByte(3);
            buf.writeShort(flowRule.getIdleTimeout());
            buf.writeShort(flowRule.getHardTimeout());
            buf.writeShort(flowRule.getPriority());
            buf.writeInt(-1);
            buf.writeInt(-1);
            buf.writeInt(-1);
            buf.writeShort(0);
            buf.writeShort(0);
            
            writeMatchFields(buf, flowRule);
            
            channel.writeAndFlush(new OpenFlowMessage(
                (byte) 0x04,
                OpenFlowMessage.Type.FLOW_MOD,
                (short) buf.readableBytes(),
                generateXid(),
                buf
            ));
            logger.info("Sent flow delete message to device: {}", device.getId());
        } else {
            logger.error("Device {} is not connected", device.getId());
        }
    }

    public void updateFlowMod(NetworkDevice device, FlowRuleDocument flowRule) {
        Channel channel = getChannelForDevice(device);
        if (channel != null && channel.isActive()) {
            ByteBuf buf = Unpooled.buffer();
            
            buf.writeLong(flowRule.getCookie());
            buf.writeLong(0L);
            buf.writeByte(flowRule.getTableId());
            buf.writeByte(1);
            buf.writeShort(flowRule.getIdleTimeout());
            buf.writeShort(flowRule.getHardTimeout());
            buf.writeShort(flowRule.getPriority());
            buf.writeInt(-1);
            buf.writeInt(-1);
            buf.writeInt(-1);
            buf.writeShort(0);
            buf.writeShort(0);
            
            writeMatchFields(buf, flowRule);
            writeInstructions(buf, flowRule);
            
            channel.writeAndFlush(new OpenFlowMessage(
                (byte) 0x04,
                OpenFlowMessage.Type.FLOW_MOD,
                (short) buf.readableBytes(),
                generateXid(),
                buf
            ));
            logger.info("Sent flow modify message to device: {}", device.getId());
        } else {
            logger.error("Device {} is not connected", device.getId());
        }
    }

    public void sendFlowStatsRequest(NetworkDevice device) {
        Channel channel = getChannelForDevice(device);
        if (channel != null && channel.isActive()) {
            ByteBuf buf = Unpooled.buffer();
            
            buf.writeByte(0); // table_id (OFPTT_ALL)
            buf.writeByte(0); // pad
            buf.writeShort(0); // out_port (OFPP_ANY)
            buf.writeShort(0); // out_group (OFPG_ANY)
            buf.writeInt(0); // pad
            buf.writeLong(0L); // cookie
            buf.writeLong(0L); // cookie_mask
            
            channel.writeAndFlush(new OpenFlowMessage(
                (byte) 0x04,
                OpenFlowMessage.Type.STATS_REQUEST,
                (short) buf.readableBytes(),
                generateXid(),
                buf
            ));
            logger.info("Sent flow stats request to device: {}", device.getId());
        } else {
            logger.error("Device {} is not connected", device.getId());
        }
    }

    public void synchronizeFlowRules(NetworkDevice device) {
        sendFlowStatsRequest(device);
    }

    private Channel getChannelForDevice(NetworkDevice device) {
        for (Channel channel : switchConnections.values()) {
            UUID deviceId = channel.attr(DEVICE_ID_KEY).get();
            if (deviceId != null && deviceId.equals(device.getId())) {
                return channel;
            }
        }
        return null;
    }

    private int generateXid() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }

    private void writeMatchFields(ByteBuf buf, FlowRuleDocument flowRule) {
        Map<String, String> matchFields = flowRule.getMatchFields();
        if (matchFields != null && !matchFields.isEmpty()) {
            // Write match header
            buf.writeShort(1); // type (OFPMT_OXM)
            int lengthPos = buf.writerIndex();
            buf.writeShort(4); // length placeholder
            
            // Write match fields
            for (Map.Entry<String, String> entry : matchFields.entrySet()) {
                writeMatchField(buf, entry.getKey(), entry.getValue());
            }
            
            // Update length
            int currentPos = buf.writerIndex();
            buf.setShort(lengthPos, currentPos - lengthPos + 4);
        } else {
            // Empty match
            buf.writeShort(1); // type (OFPMT_OXM)
            buf.writeShort(4); // length
        }
    }

    private void writeMatchField(ByteBuf buf, String key, String value) {
        // Implementation depends on your match field format
        // This is a simplified version
        buf.writeBytes(key.getBytes());
        buf.writeBytes(value.getBytes());
    }

    private void writeInstructions(ByteBuf buf, FlowRuleDocument flowRule) {
        Map<String, String> actions = flowRule.getActions();
        if (actions != null && !actions.isEmpty()) {
            // Write instruction header
            buf.writeShort(4); // type (OFPIT_APPLY_ACTIONS)
            int lengthPos = buf.writerIndex();
            buf.writeShort(8); // length placeholder
            
            // Write actions
            for (Map.Entry<String, String> entry : actions.entrySet()) {
                writeAction(buf, entry.getKey(), entry.getValue());
            }
            
            // Update length
            int currentPos = buf.writerIndex();
            buf.setShort(lengthPos, currentPos - lengthPos + 4);
        }
    }

    private void writeAction(ByteBuf buf, String type, String value) {
        // Implementation depends on your action format
        // This is a simplified version
        buf.writeBytes(type.getBytes());
        buf.writeBytes(value.getBytes());
    }
}
