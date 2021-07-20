package com.zfoo.net.session.model;

import com.zfoo.net.packet.model.IPacketAttachment;
import com.zfoo.net.packet.model.SignalPacketAttachment;
import com.zfoo.protocol.util.StringUtils;
import io.netty.channel.Channel;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author islandempty
 * @since 2021/7/14
 **/
public class Session {

    private static final AtomicLong ATOMIC_LONG = new AtomicLong(0);

    /**
     * session的id
     */
    private long sid;

    private Channel channel;

    /**
     * Session附带的参数属性
     */
    private Map<AttributeType, Object> attributes =new EnumMap<>(AttributeType.class);

    /**
     * 客户端session控制同步或异步的附加包, Key packetId
     */
    private Map<Integer, SignalPacketAttachment> clientSignalPacketAttachmentMap = new ConcurrentHashMap<>();

    public Session(Channel channel){
        if (channel == null){
            throw new IllegalArgumentException("channel不能为空");
        }
        this.sid = ATOMIC_LONG.getAndIncrement();
        this.channel =channel;
    }

    public void addClientSignalAttachment(SignalPacketAttachment signalPacketAttachment){
        clientSignalPacketAttachmentMap.put(signalPacketAttachment.getPacketId(),signalPacketAttachment);
    }

    public IPacketAttachment removeClientSignalAttachment(SignalPacketAttachment signalPacketAttachment){
        return clientSignalPacketAttachmentMap.remove(signalPacketAttachment.getPacketId());
    }

    @Override
    public String toString(){
        return StringUtils.format("[sid:{}] [channel:{}] [attributes:{}]", sid, channel, attributes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return sid == session.sid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sid);
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public synchronized void putAttribute(AttributeType key, Object value) {
        attributes.put(key, value);
    }

    public synchronized void removeAttribute(AttributeType key) {
        attributes.remove(key);
    }


    public Object getAttribute(AttributeType key) {
        return attributes.get(key);
    }

    public Map<Integer, IPacketAttachment> getClientSignalPacketAttachmentMap() {
        //产生一个只能读的map
        return Collections.unmodifiableMap(clientSignalPacketAttachmentMap);
    }

    public Channel getChannel() {
        return channel;
    }

    public void close() {
        channel.close();
        clientSignalPacketAttachmentMap.clear();
    }
}

