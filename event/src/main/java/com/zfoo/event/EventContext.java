package com.zfoo.event;

import com.ie.util.ThreadUtils;
import com.zfoo.event.manager.EventBus;
import com.zfoo.protocol.exception.ExceptionUtils;
import com.zfoo.protocol.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;

/**
 * @author islandempty
 * @since 2021/6/28
 **/
public class EventContext implements ApplicationListener<ApplicationContextEvent> , Ordered {

    private static final Logger logger = LoggerFactory.getLogger(EventContext.class);

    private static EventContext instance;

    private ApplicationContext applicationContext;

    public static EventContext getEventContext(){
        return instance;
    }

    public static ApplicationContext getApplicationContext(){
        return instance.applicationContext;
    }

    public synchronized static void shutdown(){
        try {
            Field field = EventBus.class.getDeclaredField("executors");
            ReflectionUtils.makeAccessible(field);

            //得到field字段上的值
            var executors = (ExecutorService[])ReflectionUtils.getField(field, null);
            for (ExecutorService executor : executors){
                ThreadUtils.shutdown(executor);
            }
        } catch (Throwable t) {
            logger.error("Event thread pool failed shutdown: " + ExceptionUtils.getMessage(t));
            return;
        }

        logger.info("Event shutdown gracefully.");
    }
    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        //ContextRefreshedEvent 容器初始化的时候触发
        if (event instanceof ContextRefreshedEvent){
            //初始化上下文
            EventContext.instance = this;
            instance.applicationContext = event.getApplicationContext();
        }else if (event instanceof ContextClosedEvent){
            //容器关闭时
            shutdown();
            ThreadUtils.shutdownForkJoinPool();
        }
    }

    @Override
    public int getOrder() {
        return 30;
    }
}

