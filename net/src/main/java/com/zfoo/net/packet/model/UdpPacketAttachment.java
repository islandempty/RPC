package com.zfoo.net.packet.model;

import com.ie.util.math.RandomUtils;

/**
 * @author islandempty
 * @since 2021/7/13
 **/
public class UdpPacketAttachment implements IPacketAttachment{

    public static final transient short PROTOCOL_ID = 2;

    private String host;
    private int port;

    public static UdpPacketAttachment valueOf(String host, int port) {
        var attachment = new UdpPacketAttachment();
        attachment.host = host;
        attachment.port = port;
        return attachment;
    }

    @Override
    public PacketAttachmentType packetType() {
        return PacketAttachmentType.UDP_PACKET;
    }

    /**
     * 用来确定这条消息在哪一个线程处理
     *
     * @return 一致性hashId
     */
    @Override
    public int executorConsistentHash() {
        return RandomUtils.randomInt();
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

