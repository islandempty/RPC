package com.zfoo.net.session.model;

import java.util.function.Consumer;

/**
 * @author islandempty
 * @since 2021/7/14
 **/
public enum AttributeType {

    CHANNEL_REMOTE_ADDRESS,

    /**
     * 一般是客户端session
     */
    CONSUMER,

    RESPONSE_TIME,

    /**
     * session的uid
     */
    UID,

    /**
     * 网关ip
     */
    GATEWAY_HOST_AND_PORT,

    ;

}

