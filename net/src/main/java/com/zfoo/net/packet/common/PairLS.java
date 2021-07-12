package com.zfoo.net.packet.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zfoo.protocol.IPacket;

/**
 * @author islandempty
 * @since 2021/7/12
 **/
public class PairLS implements IPacket {

    public static final transient short PROTOCOL_ID = 113;

    @JsonSerialize(using = ToStringSerializer.class)
    private long key;

    private String value;

    public static PairLS valueOf(long key, String value) {
        var pair = new PairLS();
        pair.key = key;
        pair.value = value;
        return pair;
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

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}


