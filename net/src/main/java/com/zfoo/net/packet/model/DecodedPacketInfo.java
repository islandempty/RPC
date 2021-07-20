package com.zfoo.net.packet.model;

import com.zfoo.protocol.IPacket;

/**
 * @author islandempty
 * @since 2021/7/13
 **/
public class DecodedPacketInfo {
    /**
     * 解码后的包
     */
    private IPacket packet;

    /**
     * 解码后的包的附加包
     */
    private IPacketAttachment packetAttachment;


    public static DecodedPacketInfo valueOf(IPacket packet, IPacketAttachment packetAttachment) {
        DecodedPacketInfo packetInfo = new DecodedPacketInfo();
        packetInfo.packet = packet;
        packetInfo.packetAttachment = packetAttachment;
        return packetInfo;
    }


    public IPacket getPacket() {
        return packet;
    }

    public void setPacket(IPacket packet) {
        this.packet = packet;
    }

    public IPacketAttachment getPacketAttachment() {
        return packetAttachment;
    }

    public void setPacketAttachment(IPacketAttachment packetAttachment) {
        this.packetAttachment = packetAttachment;
    }
}

