package com.zfoo.net.packet.service;

import com.zfoo.net.packet.model.DecodedPacketInfo;
import com.zfoo.net.packet.model.IPacketAttachment;
import com.zfoo.protocol.IPacket;
import io.netty.buffer.ByteBuf;
import org.springframework.lang.Nullable;

public interface IPacketService {

    void init();

    DecodedPacketInfo read(ByteBuf buffer);

    void write(ByteBuf buffer, IPacket packet, @Nullable IPacketAttachment packetAttachment);

}
