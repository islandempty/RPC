package com.zfoo.net.packet.common;

import com.zfoo.protocol.IPacket;

/**
 * @author islandempty
 * @since 2021/7/13
 **/
public class Pong implements IPacket {

    public static final transient short PROTOCOL_ID = 104;

    /**
     * 服务器当前的时间戳
     */
    private long time;

    public static Pong valueOf(long time) {
        var pong = new Pong();
        pong.time = time;
        return pong;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

