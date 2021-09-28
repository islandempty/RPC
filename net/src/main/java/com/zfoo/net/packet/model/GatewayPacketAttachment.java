package com.zfoo.net.packet.model;

import com.zfoo.util.math.HashUtils;
import com.zfoo.net.session.model.AttributeType;
import com.zfoo.net.session.model.Session;
import org.springframework.lang.Nullable;

/**
 * @author islandempty
 * @since 2021/7/14
 **/
public class GatewayPacketAttachment implements IPacketAttachment{

    public static final transient short PROTOCOL_ID = 1;

    /**
     * session的id，一般是客户端连接网关的那个sid
     */
    private long sid;

    /**
     * 用戶Id，从网关转发到后面的消息必须要附带用户的Id信息，要不然无法知道是哪个用户发过来的，0代表没有用户id
     */
    private long uid;

    /**
     * 是否使用consistentHashId作为一致性hashId
     */
    private boolean useExecutorConsistentHash;
    /**
     * 用来在TaskManage中计算一致性hash的参数
     */
    private int executorConsistentHash;

    /**
     * true为客户端，false为服务端
     */
    private boolean client;


    /**
     * 客户端发到网关的可能是一个带有同步或者异步的附加包，网关转发的时候需要把这个附加包给带上
     */
    private SignalPacketAttachment signalPacketAttachment;


    public GatewayPacketAttachment() {
    }

    public GatewayPacketAttachment(Session session, @Nullable SignalPacketAttachment signalPacketAttachment) {
        this.client = true;
        this.sid = session.getSid();
        var uid = session.getAttribute(AttributeType.UID);
        this.uid = uid == null ? 0 : (long) uid;
        this.signalPacketAttachment = signalPacketAttachment;
    }

    public GatewayPacketAttachment(long sid, long uid) {
        this.sid = sid;
        this.uid = uid;
    }


    @Override
    public PacketAttachmentType packetType() {
        return PacketAttachmentType.GATEWAY_PACKET;
    }

    @Override
    public int executorConsistentHash() {
        if (useExecutorConsistentHash) {
            return executorConsistentHash;
        } else {
            return HashUtils.fnvHash(uid);
        }
    }

    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public void useExecutorConsistentHash(Object argument) {
        this.useExecutorConsistentHash = true;
        this.executorConsistentHash = HashUtils.fnvHash(argument);
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean isUseExecutorConsistentHash() {
        return useExecutorConsistentHash;
    }

    public void setUseExecutorConsistentHash(boolean useExecutorConsistentHash) {
        this.useExecutorConsistentHash = useExecutorConsistentHash;
    }

    public int getExecutorConsistentHash() {
        return executorConsistentHash;
    }

    public void setExecutorConsistentHash(int executorConsistentHash) {
        this.executorConsistentHash = executorConsistentHash;
    }

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }


    public SignalPacketAttachment getSignalPacketAttachment() {
        return signalPacketAttachment;
    }

    public void setSignalPacketAttachment(SignalPacketAttachment signalPacketAttachment) {
        this.signalPacketAttachment = signalPacketAttachment;
    }
}

