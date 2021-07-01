package com.zfoo.event;

import com.zfoo.event.model.event.IEvent;

/**
 * @author islandempty
 * @since 2021/7/1
 **/
public class MyNoticeEvent implements IEvent {
    private String message;

    public static MyNoticeEvent valueOf(String message){
        MyNoticeEvent event = new MyNoticeEvent();
        event.setMessage(message);
        return event;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

