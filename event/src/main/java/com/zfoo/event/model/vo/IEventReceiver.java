package com.zfoo.event.model.vo;

import com.zfoo.event.model.event.IEvent;

/**
 * @author islandempty
 * @since 2021/6/28
 **/
public interface IEventReceiver {
    void invoke(IEvent event);
}

