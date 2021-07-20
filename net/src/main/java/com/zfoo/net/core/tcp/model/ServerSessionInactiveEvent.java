package com.zfoo.net.core.tcp.model;

import com.zfoo.event.model.event.IEvent;
import com.zfoo.net.session.model.Session;

/**
 * @author islandempty
 * @since 2021/7/18
 **/
public class ServerSessionInactiveEvent implements IEvent {

    private Session session;

    public static ServerSessionInactiveEvent valueOf(Session session) {
        var event = new ServerSessionInactiveEvent();
        event.session = session;
        return event;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}

