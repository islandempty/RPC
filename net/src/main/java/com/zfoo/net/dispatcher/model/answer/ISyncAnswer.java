package com.zfoo.net.dispatcher.model.answer;

import com.zfoo.net.packet.model.SignalPacketAttachment;
import com.zfoo.protocol.IPacket;

public interface ISyncAnswer<T extends IPacket> {
    /**
     *
     * @return 请求的返回包
     */
    T packet();

    /**
     *
     * @return 同步和异步的附加包
     */
    SignalPacketAttachment attachment();
}
