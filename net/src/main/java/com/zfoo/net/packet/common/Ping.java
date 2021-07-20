package com.zfoo.net.packet.common;

import com.zfoo.protocol.IPacket;

/**
 * @author islandempty
 * @since 2021/7/13
 **/
public class Ping implements IPacket {

    public static final transient short PROTOCOL_ID = 103;

    public static short pingProtocolId() {
        return PROTOCOL_ID;
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
}

