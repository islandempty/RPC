package com.zfoo.net.packet.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zfoo.protocol.IPacket;

/**
 * @author islandempty
 * @since 2021/7/13
 **/
public class TripleLSS implements IPacket {

    public static final transient short PROTOCOL_ID = 116;

    @JsonSerialize(using = ToStringSerializer.class)
    private long left;
    private String middle;
    private String right;

    public static TripleLSS valueOf(long left, String middle, String right) {
        var triple = new TripleLSS();
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

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }
}

