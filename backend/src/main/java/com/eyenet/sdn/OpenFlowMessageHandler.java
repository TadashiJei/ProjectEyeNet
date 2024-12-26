package com.eyenet.sdn;

import com.eyenet.model.document.FlowRuleDocument;
import com.eyenet.model.document.NetworkDeviceDocument;
import com.eyenet.model.entity.FlowRule;
import com.eyenet.model.entity.NetworkDevice;
import com.eyenet.mapper.FlowRuleMapper;
import com.eyenet.mapper.NetworkDeviceMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

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
            if (payload.readableBytes() < 24) { // Minimum size for packet-in header
                logger.error("Packet-in message too short");
                return;
            }

            // Extract packet metadata
            int bufferId = payload.readInt();
            int totalLen = payload.readShort() & 0xFFFF; // Convert to unsigned
            byte reason = payload.readByte();
            byte tableId = payload.readByte();
            long cookie = payload.readLong();
            
            // Process packet based on reason
            switch (reason) {
                case 0: // OFPR_NO_MATCH
                    handleNoMatchPacket(ctx, payload, bufferId);
                    break;
                case 1: // OFPR_ACTION
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
        // Handle packets with no matching flow entry
        try {
            // Extract match fields and create appropriate flow rules
            // For now, we'll just log it
            logger.debug("Processing no-match packet with buffer ID: {}", bufferId);
        } catch (Exception e) {
            logger.error("Error handling no-match packet", e);
        }
    }

    private void handleActionPacket(ChannelHandlerContext ctx, ByteBuf payload, int bufferId) {
        // Handle packets that were sent to controller by action
        try {
            // Process action-based packet
            // For now, we'll just log it
            logger.debug("Processing action packet with buffer ID: {}", bufferId);
        } catch (Exception e) {
            logger.error("Error handling action packet", e);
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Error handling OpenFlow message", cause);
        ctx.close();
    }

    public void sendFlowMod(NetworkDeviceDocument device, FlowRuleDocument flowRule) {
        Channel channel = getChannelForDevice(device);
        if (channel != null && channel.isActive()) {
            FlowRule entityRule = flowRuleMapper.mapToEntity(flowRule);
            ByteBuf buf = Unpooled.buffer();
            
            // Write flow mod fields
            buf.writeLong(entityRule.getCookie()); // cookie
            buf.writeLong(0L); // cookie_mask
            buf.writeByte(entityRule.getTableId()); // table_id
            buf.writeByte(0); // command (OFPFC_ADD)
            buf.writeShort(entityRule.getIdleTimeout()); // idle_timeout
            buf.writeShort(entityRule.getHardTimeout()); // hard_timeout
            buf.writeShort(entityRule.getPriority()); // priority
            buf.writeInt(-1); // buffer_id (NONE)
            buf.writeInt(-1); // out_port (ANY)
            buf.writeInt(-1); // out_group (ANY)
            buf.writeShort(0); // flags
            buf.writeShort(0); // pad
            
            // Write match fields
            writeMatchFields(buf, entityRule);
            
            // Write instructions
            writeInstructions(buf, entityRule);
            
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

    public void removeFlowMod(NetworkDeviceDocument device, FlowRuleDocument flowRule) {
        Channel channel = getChannelForDevice(device);
        if (channel != null && channel.isActive()) {
            FlowRule entityRule = flowRuleMapper.mapToEntity(flowRule);
            ByteBuf buf = Unpooled.buffer();
            
            // Write flow mod fields
            buf.writeLong(entityRule.getCookie()); // cookie
            buf.writeLong(0L); // cookie_mask
            buf.writeByte(entityRule.getTableId()); // table_id
            buf.writeByte(3); // command (OFPFC_DELETE)
            buf.writeShort(entityRule.getIdleTimeout()); // idle_timeout
            buf.writeShort(entityRule.getHardTimeout()); // hard_timeout
            buf.writeShort(entityRule.getPriority()); // priority
            buf.writeInt(-1); // buffer_id (NONE)
            buf.writeInt(-1); // out_port (ANY)
            buf.writeInt(-1); // out_group (ANY)
            buf.writeShort(0); // flags
            buf.writeShort(0); // pad
            
            // Write match fields
            writeMatchFields(buf, entityRule);
            
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

    public void updateFlowMod(NetworkDeviceDocument device, FlowRuleDocument flowRule) {
        Channel channel = getChannelForDevice(device);
        if (channel != null && channel.isActive()) {
            FlowRule entityRule = flowRuleMapper.mapToEntity(flowRule);
            ByteBuf buf = Unpooled.buffer();
            
            // Write flow mod fields
            buf.writeLong(entityRule.getCookie()); // cookie
            buf.writeLong(0L); // cookie_mask
            buf.writeByte(entityRule.getTableId()); // table_id
            buf.writeByte(1); // command (OFPFC_MODIFY)
            buf.writeShort(entityRule.getIdleTimeout()); // idle_timeout
            buf.writeShort(entityRule.getHardTimeout()); // hard_timeout
            buf.writeShort(entityRule.getPriority()); // priority
            buf.writeInt(-1); // buffer_id (NONE)
            buf.writeInt(-1); // out_port (ANY)
            buf.writeInt(-1); // out_group (ANY)
            buf.writeShort(0); // flags
            buf.writeShort(0); // pad
            
            // Write match fields
            writeMatchFields(buf, entityRule);
            
            // Write instructions
            writeInstructions(buf, entityRule);
            
            channel.writeAndFlush(new OpenFlowMessage(
                (byte) 0x04,
                OpenFlowMessage.Type.FLOW_MOD,
                (short) buf.readableBytes(),
                generateXid(),
                buf
            ));
            logger.info("Sent flow update message to device: {}", device.getId());
        } else {
            logger.error("Device {} is not connected", device.getId());
        }
    }

    private Channel getChannelForDevice(NetworkDeviceDocument device) {
        return switchConnections.values().stream()
                .filter(channel -> {
                    UUID channelDeviceId = channel.attr(DEVICE_ID_KEY).get();
                    return channelDeviceId != null && channelDeviceId.equals(device.getId());
                })
                .findFirst()
                .orElse(null);
    }

    private int generateXid() {
        return (int) (System.nanoTime() & 0xFFFFFFFF);
    }

    private void writeMatchFields(ByteBuf buf, FlowRule flowRule) {
        // Write match fields based on flow rule
        // This is a simplified version - expand based on your needs
        buf.writeShort(0); // match type
        buf.writeShort(4); // match length
        buf.writeZero(4); // pad
    }

    private void writeInstructions(ByteBuf buf, FlowRule flowRule) {
        // Write instructions based on flow rule
        // This is a simplified version - expand based on your needs
        buf.writeShort(0); // instruction type
        buf.writeShort(8); // instruction length
        buf.writeInt(0); // instruction data
    }
}
