package com.eyenet.sdn;

import io.netty.buffer.ByteBuf;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class OpenFlowMessage {
    private byte version;
    private byte type;
    private short length;
    private int xid;
    private ByteBuf payload;
    
    public byte getVersion() {
        return version;
    }
    
    public void setVersion(byte version) {
        this.version = version;
    }
    
    public byte getType() {
        return type;
    }
    
    public void setType(byte type) {
        this.type = type;
    }
    
    public short getLength() {
        return length;
    }
    
    public void setLength(short length) {
        this.length = length;
    }
    
    public int getXid() {
        return xid;
    }
    
    public void setXid(int xid) {
        this.xid = xid;
    }
    
    public ByteBuf getPayload() {
        return payload;
    }
    
    public void setPayload(ByteBuf payload) {
        this.payload = payload;
    }
    
    public void release() {
        if (payload != null) {
            payload.release();
        }
    }
    
    public static class Type {
        public static final byte HELLO = 0;
        public static final byte ERROR = 1;
        public static final byte ECHO_REQUEST = 2;
        public static final byte ECHO_REPLY = 3;
        public static final byte FEATURES_REQUEST = 5;
        public static final byte FEATURES_REPLY = 6;
        public static final byte PACKET_IN = 10;
        public static final byte FLOW_REMOVED = 11;
        public static final byte PORT_STATUS = 12;
    }
}
