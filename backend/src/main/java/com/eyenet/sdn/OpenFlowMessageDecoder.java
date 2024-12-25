package com.eyenet.sdn;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OpenFlowMessageDecoder extends ByteToMessageDecoder {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenFlowMessageDecoder.class);
    private static final int HEADER_LENGTH = 8;
    
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < HEADER_LENGTH) {
            return;
        }
        
        in.markReaderIndex();
        
        // Read OpenFlow header
        byte version = in.readByte();
        byte type = in.readByte();
        short length = in.readShort();
        int xid = in.readInt();
        
        if (in.readableBytes() < length - HEADER_LENGTH) {
            in.resetReaderIndex();
            return;
        }
        
        // Read message payload
        ByteBuf payload = in.readBytes(length - HEADER_LENGTH);
        
        // Create OpenFlow message object and set its fields
        OpenFlowMessage message = new OpenFlowMessage();
        message.setVersion(version);
        message.setType(type);
        message.setLength(length);
        message.setXid(xid);
        message.setPayload(payload);
        
        out.add(message);
        
        logger.debug("Decoded OpenFlow message: version={}, type={}, length={}, xid={}",
                version, type, length, xid);
    }
}
