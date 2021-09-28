package com.zfoo.net.packet.model;

import com.zfoo.util.security.IdUtils;
import com.zfoo.protocol.IPacket;
import com.zfoo.scheduler.util.TimeUtils;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 附加包对业务层透明，禁止在业务层使用
 *
 * @author islandempty
 * @since 2021/7/14
 **/
public class SignalPacketAttachment implements IPacketAttachment{

    public static final transient short PROTOCOL_ID = 0;

    private static final AtomicInteger ATOMIC_PACKET_ID = new AtomicInteger(0);

    /**
     * 唯一标识一个packet， 唯一表示一个PacketAttachment，hashcode() and equals() 也通过packetId计算
     */
    private int packetId = ATOMIC_PACKET_ID.incrementAndGet();

    /**
     * 用来在TaskManage中计算一致性hash的参数
     */
    private int executorConsistentHash = -1;

    /**
     * true为客户端，false为服务端
     */
    private boolean client = true;

    /**
     * 客户端发送的时间
     */
    private transient long timestamp = TimeUtils.now();

    /**
     * 客户端收到服务器回复的时候回调的方法
     */
    private transient CompletableFuture<IPacket> responseFuture = new CompletableFuture<>();

    public SignalPacketAttachment() {
    }


    @Override
    public PacketAttachmentType packetType() {
        return PacketAttachmentType.SIGNAL_PACKET;
    }

    @Override
    public int executorConsistentHash() {
        return executorConsistentHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SignalPacketAttachment that = (SignalPacketAttachment) o;
        return Objects.equals(packetId, that.packetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packetId);
    }

    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
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


    public CompletableFuture<IPacket> getResponseFuture() {
        return responseFuture;
    }

    public void setResponseFuture(CompletableFuture<IPacket> responseFuture) {
        this.responseFuture = responseFuture;
    }
}

