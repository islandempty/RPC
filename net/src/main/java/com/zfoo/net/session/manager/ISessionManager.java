package com.zfoo.net.session.manager;

import com.zfoo.net.session.model.Session;

import java.util.Map;

/**
 * @author islandempty
 * @since 2021/7/15
 **/
public interface ISessionManager {

    void addServerSession(Session session);

    void removeServerSession(Session session);

    Session getServerSession(Long id);

    Map<Long, Session> getServerSessionMap();


    void addClientSession(Session session);

    void removeClientSession(Session session);

    Session getClientSession(Long id);

    Map<Long, Session> getClientSessionMap();

    int getClientSessionChangeId();
}

