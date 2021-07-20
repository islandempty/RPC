package com.zfoo.net.consumer.event;

import com.zfoo.event.model.event.IEvent;
import com.zfoo.net.consumer.registry.RegisterVO;
import com.zfoo.net.session.model.Session;

/**
 * @author islandempty
 * @since 2021/7/18
 **/
public class ConsumerStartEvent implements IEvent {
    private RegisterVO registerVO;
    private Session session;

    public static ConsumerStartEvent valueOf(RegisterVO registerVO, Session session) {
        var event = new ConsumerStartEvent();
        event.registerVO = registerVO;
        event.session = session;
        return event;
    }

    public RegisterVO getRegisterVO() {
        return registerVO;
    }

    public void setRegisterVO(RegisterVO registerVO) {
        this.registerVO = registerVO;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}

