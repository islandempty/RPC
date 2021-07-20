package com.zfoo.net.dispatcher.model.exception;

import com.zfoo.net.packet.common.Error;

/**
 * @author islandempty
 * @since 2021/7/18
 **/
public class ErrorResponseException extends RuntimeException{

    public ErrorResponseException(Error error) {
        super(error.toString());
    }
}

