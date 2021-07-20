package com.zfoo.net.core.gateway.model;

import com.zfoo.event.model.event.IEvent;

/**
 * @author islandempty
 * @since 2021/7/18
 **/
public class GatewaySessionInactiveEvent implements IEvent {
    private long sid;
    private long uid;

    public static GatewaySessionInactiveEvent valueOf(long sid, long uid) {
        var event = new GatewaySessionInactiveEvent();
        event.sid = sid;
        event.uid = uid;
        return event;
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
}

