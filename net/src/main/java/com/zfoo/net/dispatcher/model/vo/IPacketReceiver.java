package com.zfoo.net.dispatcher.model.vo;

import com.zfoo.net.packet.model.IPacketAttachment;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;

public interface IPacketReceiver {
    void invoke(Session session, IPacket packet, IPacketAttachment attachment);
}
