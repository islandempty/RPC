package com.zfoo.event;

import com.zfoo.util.ThreadUtils;
import com.zfoo.event.manager.EventBus;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author islandempty
 * @since 2021/7/1
 **/

@Ignore
public class ApplicationTest {

    @Test
    public void Test(){
        // 加载配置文件，配置文件中必须引入event
        var context = new ClassPathXmlApplicationContext("application.xml");

        // 事件的接受需要在被Spring管理的bean的方法上加上@EventReceiver注解，即可自动注册事件的监听
        // 抛出同步事件，事件会被当前线程立刻执行，注意日志打印的线程号
        EventBus.syncSubmit(MyNoticeEvent.valueOf("这是同步事件"));

        // 抛出异步事件，事件会被不会立刻执行，注意日志打印的线程号
        EventBus.asyncSubmit(MyNoticeEvent.valueOf("这是异步事件"));

        //等待异步事件执行完
        ThreadUtils.sleep(3000);
    }
}

