package com.zfoo.scheduler;

import com.zfoo.util.ThreadUtils;
import com.zfoo.protocol.util.ReflectionUtils;
import com.zfoo.scheduler.manager.SchedulerBus;
import com.zfoo.scheduler.schema.SchedulerRegisterProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import java.lang.reflect.Field;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author islandempty
 * @since 2021/7/2
 **/
public class SchedulerContext implements ApplicationListener<ApplicationContextEvent>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerContext.class);

    private static SchedulerContext instance;

    private static boolean stop = false;

    private ApplicationContext applicationContext;

    public static SchedulerContext getSchedulerContext() {
        return instance;
    }

    public static boolean isStop() {
        return stop;
    }

    public static ApplicationContext getApplicationContext() {
        return instance.applicationContext;
    }

    public synchronized static void shutdown(){
        if (stop){
            return;
        }

        stop=true;
        try {
            Field field = SchedulerBus.class.getDeclaredField("executor");
            ReflectionUtils.makeAccessible(field);
            //获得指定对象obj上此 Field 表示的字段的值
            var executor = (ScheduledExecutorService) ReflectionUtils.getField(field,null);
            ThreadUtils.shutdown(executor);
        } catch (Throwable e) {
            logger.error("Scheduler thread pool failed shutdown.", e);
            return;
        }
        logger.info("Scheduler shutdown gracefully.");
    }
    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent){
            if (instance !=null){
                return;
            }
            //初始化上下文
            SchedulerContext.instance = this;
            instance.applicationContext = event.getApplicationContext();
        }else if (event instanceof ContextClosedEvent){
            shutdown();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

