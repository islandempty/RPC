package com.zfoo.net.core.gateway.model;

import com.zfoo.protocol.IPacket;

import java.util.Map;

/**
 * @author islandempty
 * @since 2021/7/18
 **/
public class GatewaySynchronizeSidAsk implements IPacket {
    public static final transient short PROTOCOL_ID = 24;

    private String gatewayHostAndPort;

    private Map<Long, Long> sidMap;

    public static GatewaySynchronizeSidAsk valueOf(String gatewayHostAndPort, Map<Long, Long> sidMap) {
        var ask = new GatewaySynchronizeSidAsk();
        ask.gatewayHostAndPort = gatewayHostAndPort;
        ask.sidMap = sidMap;
        return ask;
    }

    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public static short gatewaySynchronizeProtocolId() {
        return PROTOCOL_ID;
    }

    public Map<Long, Long> getSidMap() {
        return sidMap;
    }

    public void setSidMap(Map<Long, Long> sidMap) {
        this.sidMap = sidMap;
    }

    public String getGatewayHostAndPort() {
        return gatewayHostAndPort;
    }

    public void setGatewayHostAndPort(String gatewayHostAndPort) {
        this.gatewayHostAndPort = gatewayHostAndPort;
    }
}

