package com.zfoo.net.core.gateway.model;

import com.zfoo.protocol.IPacket;

/**
 * @author islandempty
 * @since 2021/7/18
 **/
public class AuthUidAsk implements IPacket {

    public static final transient short PROTOCOL_ID = 22;

    private String gatewayHostAndPort;

    private long sid;
    private long uid;

    public static AuthUidAsk valueOf(String gatewayHostAndPort, long sid, long uid) {
        var ask = new AuthUidAsk();
        ask.gatewayHostAndPort = gatewayHostAndPort;
        ask.sid = sid;
        ask.uid = uid;
        return ask;
    }


    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public String getGatewayHostAndPort() {
        return gatewayHostAndPort;
    }

    public void setGatewayHostAndPort(String gatewayHostAndPort) {
        this.gatewayHostAndPort = gatewayHostAndPort;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}

