package com.eyenet.sdn;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class OpenFlowMessageHandler extends SimpleChannelInboundHandler<OpenFlowMessage> {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenFlowMessageHandler.class);
    private final Map<ChannelId, Channel> switchConnections;
    
    public OpenFlowMessageHandler(Map<ChannelId, Channel> switchConnections) {
        this.switchConnections = switchConnections;
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("New switch connected from: {}", ctx.channel().remoteAddress());
        switchConnections.put(ctx.channel().id(), ctx.channel());
        
        // Send HELLO message
        sendHello(ctx);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("Switch disconnected: {}", ctx.channel().remoteAddress());
        switchConnections.remove(ctx.channel().id());
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, OpenFlowMessage msg) {
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
        } finally {
            msg.release();
        }
    }
    
    private void handleHello(ChannelHandlerContext ctx, OpenFlowMessage msg) {
        logger.info("Received HELLO from switch: {}", ctx.channel().remoteAddress());
        // Send Features Request
        sendFeaturesRequest(ctx);
    }
    
    private void handleFeaturesRequest(ChannelHandlerContext ctx, OpenFlowMessage msg) {
        logger.info("Received FEATURES_REQUEST from switch: {}", ctx.channel().remoteAddress());
        // TODO: Send features reply
    }
    
    private void handlePacketIn(ChannelHandlerContext ctx, OpenFlowMessage msg) {
        logger.info("Received PACKET_IN from switch: {}", ctx.channel().remoteAddress());
        // TODO: Implement packet processing logic
    }
    
    private void sendHello(ChannelHandlerContext ctx) {
        ByteBuf buf = ctx.alloc().buffer(8);
        buf.writeByte(0x04); // version
        buf.writeByte(OpenFlowMessage.Type.HELLO); // type
        buf.writeShort(8); // length
        buf.writeInt(0); // xid
        
        ctx.writeAndFlush(buf);
        logger.debug("Sent HELLO to switch: {}", ctx.channel().remoteAddress());
    }
    
    private void sendFeaturesRequest(ChannelHandlerContext ctx) {
        ByteBuf buf = ctx.alloc().buffer(8);
        buf.writeByte(0x04); // version
        buf.writeByte(OpenFlowMessage.Type.FEATURES_REQUEST); // type
        buf.writeShort(8); // length
        buf.writeInt(1); // xid
        
        ctx.writeAndFlush(buf);
        logger.debug("Sent FEATURES_REQUEST to switch: {}", ctx.channel().remoteAddress());
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Error handling OpenFlow message", cause);
        ctx.close();
    }
}
