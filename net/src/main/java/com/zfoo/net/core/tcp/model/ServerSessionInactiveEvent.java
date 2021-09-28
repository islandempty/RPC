/*
 * Copyright (C) 2020 The zfoo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.net.core.tcp.model;

import com.zfoo.event.model.event.IEvent;
import com.zfoo.net.session.model.Session;

/**
 * @author islandempty
 * @since 2021/7/24
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
