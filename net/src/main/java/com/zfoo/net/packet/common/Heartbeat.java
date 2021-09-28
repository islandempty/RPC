package com.zfoo.net.packet.common;

import com.zfoo.protocol.IPacket;

/**
 * @author islandempty
 * @since 2021/7/12
 **/
public class Heartbeat implements IPacket {

    public static final transient short PROTOCOL_ID = 102;

    private static Heartbeat INSTANCE = new Heartbeat();

    public static Heartbeat getInstance(){
        return INSTANCE;
    }
    /**
     * 这个类的协议号
     *
     * @return 协议号
     */
    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public static short heartbeatProtocolId(){
        return PROTOCOL_ID;
    }
}

