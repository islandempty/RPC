package com.zfoo.net.packet.model;

/**
 * @author islandempty
 * @since 2021/7/14
 **/
public class NoAnswerAttachment implements IPacketAttachment{

    public static final transient short PROTOCOL_ID = 4;

    /**
     * 用来在TaskManage中计算一致性hash的参数
     */
    private int executorConsistentHash;

    public static NoAnswerAttachment valueOf(int executorConsistentHash) {
        var attachment = new NoAnswerAttachment();
        attachment.executorConsistentHash = executorConsistentHash;
        return attachment;
    }


    @Override
    public PacketAttachmentType packetType() {
        return PacketAttachmentType.NO_ANSWER_PACKET;
    }

    /**
     * 用来确定这条消息在哪一个线程处理
     *
     * @return 一致性hashId
     */
    @Override
    public int executorConsistentHash() {
        return executorConsistentHash;
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

    public int getExecutorConsistentHash() {
        return executorConsistentHash;
    }

    public void setExecutorConsistentHash(int executorConsistentHash) {
        this.executorConsistentHash = executorConsistentHash;
    }
}

