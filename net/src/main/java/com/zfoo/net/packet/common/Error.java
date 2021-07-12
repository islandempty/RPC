package com.zfoo.net.packet.common;

import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.ProtocolManager;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author islandempty
 * @since 2021/7/5
 **/
public class Error implements IPacket {

    public static final transient short PROTOCOL_ID = 101;

    private int module;

    private int errorCode;

    private String errorMessage;

    /**
     * 这个类的协议号
     *
     * @return 协议号
     */
    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public static short errorProtocolId() {
        return PROTOCOL_ID;
    }

    @Override
    public String toString() {
        FormattingTuple message = MessageFormatter.arrayFormat(
                "module:[{}], errorCode:[{}], errorMessage:[{}]", new Object[]{module, errorCode, errorMessage});
        return message.getMessage();
    }

    public static Error valueOf(int module, int errorCode, String errorMessage){
        Error error = new Error();
        error.module = module;
        error.errorCode = errorCode;
        error.errorMessage = errorMessage;
        return error;
    }

    public static Error valueOf(IPacket iPacket, int errorCode, String errorMessage){
        Error error = new Error();
        error.module = ProtocolManager.getProtocol(iPacket.protocolId()).module();
        error.errorCode = errorCode;
        error.errorMessage = errorMessage;
        return error;
    }

    public static Error valueOf(IPacket packet, int errorCode) {
        return valueOf(packet, errorCode, null);
    }

    public static Error valueOf(IPacket packet, String errorMessage) {
        return valueOf(packet, 0, errorMessage);
    }

    public static Error valueOf(String errorMessage) {
        return valueOf(0, 0, errorMessage);
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

