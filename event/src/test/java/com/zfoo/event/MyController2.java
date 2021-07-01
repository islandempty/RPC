package com.zfoo.event;

import com.zfoo.event.model.anno.EventReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author islandempty
 * @since 2021/7/1
 **/

@Component
public class MyController2 {
    public static final Logger logger = LoggerFactory.getLogger(MyController2.class);

    @EventReceiver
    public void onMyNoticeEvent(MyNoticeEvent event) {
        logger.info("这是方法2--收到事件：" + event.getMessage());
    }
}

