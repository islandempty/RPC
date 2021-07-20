package com.zfoo.net.packet.model;

import com.zfoo.protocol.IPacket;

public interface IPacketAttachment extends IPacket {

    PacketAttachmentType packetType();

    /**
     * 用来确定这条消息在哪一个线程处理
     * @return 一致性hashId
     */
    int executorConsistentHash();
}
