package com.zfoo.net.core.gateway.model;

import com.zfoo.protocol.IPacket;

/**
 *
 * 网关登录成功过后，将uid授权给网关的返回
 *
 * @author islandempty
 * @since 2021/7/18
 **/
public class AuthUidToGatewayConfirm implements IPacket {


    public static final transient short PROTOCOL_ID = 21;

    private long uid;

    public static AuthUidToGatewayConfirm valueOf(long uid) {
        var authUidToGateway = new AuthUidToGatewayConfirm();
        authUidToGateway.uid = uid;
        return authUidToGateway;
    }

    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}

