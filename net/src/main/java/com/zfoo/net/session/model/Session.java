package com.zfoo.net.session.model;

import com.zfoo.net.packet.model.IPacketAttachment;
import com.zfoo.net.packet.model.SignalPacketAttachment;
import com.zfoo.protocol.util.StringUtils;
import io.netty.channel.Channel;

import java.io.Closeable;
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
public class Session implements Closeable {

    private static final AtomicLong ATOMIC_LONG = new AtomicLong(0);

    /**
     * session的id
     */
    private long sid;

    private Channel channel;

    /**
     * Session附带的属性参数
     */
    private Map<AttributeType, Object> attributes = new EnumMap<>(AttributeType.class);


    public Session(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel不能为空");
        }
        this.sid = ATOMIC_LONG.getAndIncrement();
        this.channel = channel;
    }


    @Override
    public String toString() {
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

    @Override
    public void close() {
        channel.close();
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

    public Channel getChannel() {
        return channel;
    }
}

