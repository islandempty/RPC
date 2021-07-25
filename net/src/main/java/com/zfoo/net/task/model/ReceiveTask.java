package com.zfoo.net.task.model;

import com.zfoo.net.NetContext;
import com.zfoo.net.packet.model.IPacketAttachment;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;

/**
 * @author islandempty
 * @since 2021/7/16
 **/
public class ReceiveTask implements Runnable{
    private Session session;
    private IPacket packet;
    private IPacketAttachment packetAttachment;

    public ReceiveTask(Session session, IPacket packet, IPacketAttachment packetAttachment) {
        this.session = session;
        this.packet = packet;
        this.packetAttachment = packetAttachment;
    }

    @Override
    public void run() {
        NetContext.getDispatcher().atReceive(session, packet, packetAttachment);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
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

