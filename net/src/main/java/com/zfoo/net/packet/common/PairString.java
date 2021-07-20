package com.zfoo.net.packet.common;

import com.zfoo.protocol.IPacket;

/**
 * @author islandempty
 * @since 2021/7/13
 **/
public class PairString implements IPacket {

    public static final transient short PROTOCOL_ID = 112;

    private String key;

    private String value;

    public static PairString valueOf(String key, String value) {
        var pair = new PairString();
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

