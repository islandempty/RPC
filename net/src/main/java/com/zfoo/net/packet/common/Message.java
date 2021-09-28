package com.zfoo.net.packet.common;

import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.ProtocolManager;

/**
 * @author islandempty
 * @since 2021/7/12
 **/
public class Message implements IPacket {

    public static final transient short PROTOCOL_ID = 100;

    public static final Message SUCCESS = valueSuccess(null);
    public static final Message FAIL = valueFail(null);

    private byte module;

    /**
     * 1是成功，其它的均视为失败的请求
     */
    private int code;

    private String message;

    public boolean success() {
        return code == 1;
    }

    public boolean fail() {
        return code == 0;
    }

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

    public static Message valueFail(String message) {
        var mess = new Message();
        mess.code = 0;
        mess.message = message;
        return mess;
    }

    public static Message valueSuccess(String message) {
        var mess = new Message();
        mess.code = 1;
        mess.message = message;
        return mess;
    }

    @Override
    public short protocolId() {
        return PROTOCOL_ID;
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

