package com.zfoo.net.packet.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zfoo.protocol.IPacket;

/**
 * @author islandempty
 * @since 2021/7/13
 **/
public class TripleLong implements IPacket {

    public static final transient short PROTOCOL_ID = 114;


    @JsonSerialize(using = ToStringSerializer.class)
    private long left;
    @JsonSerialize(using = ToStringSerializer.class)
    private long middle;
    @JsonSerialize(using = ToStringSerializer.class)
    private long right;

    public static TripleLong valueOf(long left, long middle, long right) {
        var triple = new TripleLong();
        triple.left = left;
        triple.middle = middle;
        triple.right = right;
        return triple;
    }

    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public long getLeft() {
        return left;
    }

    public void setLeft(long left) {
        this.left = left;
    }

    public long getMiddle() {
        return middle;
    }

    public void setMiddle(long middle) {
        this.middle = middle;
    }

    public long getRight() {
        return right;
    }

    public void setRight(long right) {
        this.right = right;
    }
}

