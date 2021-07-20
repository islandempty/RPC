package com.zfoo.net.packet.common;

import com.zfoo.protocol.IPacket;

/**
 * @author islandempty
 * @since 2021/7/13
 **/
public class TripleString implements IPacket {

    public static final transient short PROTOCOL_ID = 115;

    private String left;
    private String middle;
    private String right;

    public static TripleString valueOf(String left, String middle, String right) {
        var triple = new TripleString();
        triple.left = left;
        triple.middle = middle;
        triple.right = right;
        return triple;
    }

    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
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

