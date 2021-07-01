package com.zfoo.event.model.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 *
 * 应用启动事件，这个使用spring自带的事件机制，自己的event事件仅用在业务逻辑
 *
 * @author islandempty
 * @since 2021/6/28
 **/
public class AppStartEvent extends ApplicationContextEvent {

    public AppStartEvent(ApplicationContext source) {
        super(source);
    }
}

