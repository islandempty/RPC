package com.zfoo.net.packet.common;

import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.ProtocolManager;

/**
 * @author islandempty
 * @since 2021/7/12
 **/
public class Message implements IPacket {

    public static final transient short PROTOCOL_ID = 100;

    private byte module;

    /**
     * 只有1位成功
     */
    private int code;

    private String message;

    public static Message valueOf(IPacket packet, int code, String message) {
        var mess = new Message();
        mess.module = ProtocolManager.moduleByProtocolId(packet.protocolId()).getId();
        mess.code = code;
        mess.message = message;
        return mess;
    }

    public static Message valueOf(IPacket packet, int code) {
        return Message.valueOf(packet, code, null);
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

    public boolean success() {
        return code == 1;
    }

    public byte getModule() {
        return module;
    }

    public void setModule(byte module) {
        this.module = module;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

