package com.eyenet.sdn;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenFlowMessage {
    private byte version;
    private byte type;
    private short length;
    private int xid;
    private ByteBuf payload;

    public static class Type {
        public static final byte HELLO = 0;
        public static final byte ERROR = 1;
        public static final byte ECHO_REQUEST = 2;
        public static final byte ECHO_REPLY = 3;
        public static final byte VENDOR = 4;
        public static final byte FEATURES_REQUEST = 5;
        public static final byte FEATURES_REPLY = 6;
        public static final byte GET_CONFIG_REQUEST = 7;
        public static final byte GET_CONFIG_REPLY = 8;
        public static final byte SET_CONFIG = 9;
        public static final byte PACKET_IN = 10;
        public static final byte FLOW_REMOVED = 11;
        public static final byte PORT_STATUS = 12;
        public static final byte PACKET_OUT = 13;
        public static final byte FLOW_MOD = 14;
        public static final byte GROUP_MOD = 15;
        public static final byte PORT_MOD = 16;
        public static final byte TABLE_MOD = 17;
        public static final byte STATS_REQUEST = 18;
        public static final byte STATS_REPLY = 19;
        public static final byte BARRIER_REQUEST = 20;
        public static final byte BARRIER_REPLY = 21;
        public static final byte QUEUE_GET_CONFIG_REQUEST = 22;
        public static final byte QUEUE_GET_CONFIG_REPLY = 23;
        public static final byte ROLE_REQUEST = 24;
        public static final byte ROLE_REPLY = 25;
        public static final byte GET_ASYNC_REQUEST = 26;
        public static final byte GET_ASYNC_REPLY = 27;
        public static final byte SET_ASYNC = 28;
        public static final byte METER_MOD = 29;
    }

    public void release() {
        if (payload != null && payload.refCnt() > 0) {
            payload.release();
        }
    }
}
