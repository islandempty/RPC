package com.zfoo.net.dispatcher.model.exception;

import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.exception.RunException;

/**
 * @author islandempty
 * @since 2021/7/18
 **/
public class UnexpectedProtocolException extends RunException {

    public UnexpectedProtocolException(String message) {
        super(message);
    }

    public UnexpectedProtocolException(String template, Object... args) {
        super(template, args);
    }
}

