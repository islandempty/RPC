package com.zfoo.net.dispatcher.model.answer;

import com.zfoo.net.packet.model.SignalPacketAttachment;
import com.zfoo.protocol.IPacket;

/**
 * @author islandempty
 * @since 2021/7/19
 **/
public class SyncAnswer<T extends IPacket> implements ISyncAnswer<T> {
    private T packet;
    private SignalPacketAttachment attachment;

    public SyncAnswer(T packet, SignalPacketAttachment attachment) {
        this.packet = packet;
        this.attachment = attachment;
    }

    /**
     * @return 请求的返回包
     */
    @Override
    public T packet() {
        return packet;
    }

    /**
     * @return 同步和异步的附加包
     */
    @Override
    public SignalPacketAttachment attachment() {
        return attachment;
    }
}

