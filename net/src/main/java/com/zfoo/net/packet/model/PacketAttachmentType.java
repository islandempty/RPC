package com.zfoo.net.packet.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author islandempty
 * @since 2021/7/13
 **/
public enum PacketAttachmentType {


    /**
     * 带有同步或者异步信息的附加包
     */
    SIGNAL_PACKET((byte) 0, SignalPacketAttachment.class),

    /**
     * 带有网关信息的附加包
     */
    GATEWAY_PACKET((byte) 1, GatewayPacketAttachment.class),

    /**
     * udp消息的附加包
     */
    UDP_PACKET((byte) 2, UdpPacketAttachment.class),


    /**
     * http消息的附加包
     */
    HTTP_PACKET((byte) 3, HttpPacketAttachment.class),

    /**
     * 无返回消息的附加包
     */
    NO_ANSWER_PACKET((byte) 4, NoAnswerAttachment.class),


    ;


    public static final Map<Byte, PacketAttachmentType> map = new HashMap<>(values().length);

    static {
        for (var packetType : PacketAttachmentType.values()) {
            map.put(packetType.packetType, packetType);
        }
    }

    public static PacketAttachmentType getPacketType(byte packetType) {
        return map.getOrDefault(packetType, PacketAttachmentType.NO_ANSWER_PACKET);
    }

    public byte getPacketType() {
        return packetType;
    }

    private byte packetType;
    private Class<? extends IPacketAttachment> clazz;

    PacketAttachmentType(byte packetType, Class<? extends IPacketAttachment> clazz) {
        this.packetType = packetType;
        this.clazz = clazz;
    }


}

