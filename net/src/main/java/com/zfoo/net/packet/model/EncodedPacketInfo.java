package com.zfoo.net.packet.model;

import com.zfoo.protocol.IPacket;
import org.springframework.lang.Nullable;

/**
 * 被解码后的Packet的信息
 *
 * @author islandempty
 * @since 2021/7/14
 **/
public class EncodedPacketInfo {

    /**
     * 编码后的包
     */
    private IPacket packet;

    /**
     * 编码后的包的附加包
     */
    private IPacketAttachment packetAttachment;
    /**
     * 长度
     */
    private int length;
    /**
     * 加密所用时间
     */
    private long encodedTime;

    public static EncodedPacketInfo valueOf(IPacket packet, @Nullable IPacketAttachment packetAttachment) {
        EncodedPacketInfo packetInfo = new EncodedPacketInfo();
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getEncodedTime() {
        return encodedTime;
    }

    public void setEncodedTime(long encodedTime) {
        this.encodedTime = encodedTime;
    }

}

