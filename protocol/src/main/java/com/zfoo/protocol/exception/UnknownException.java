package com.zfoo.protocol.exception;

import com.zfoo.protocol.util.StringUtils;

/**
 * @author islandempty
 * @since 2021/7/10
 **/
public class UnknownException extends RuntimeException{

    public UnknownException(Throwable cause) {
        super(cause);
    }

    public UnknownException(String message) {
        super(message);
    }

    public UnknownException(String template, Object... args) {
        super(StringUtils.format(template, args));
    }

    public UnknownException(Throwable cause, String message) {
        super(message, cause);
    }

    public UnknownException(Throwable cause, String template, Object... args) {
        super(StringUtils.format(template, args), cause);
    }
}

