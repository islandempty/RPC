package com.zfoo.net;

import com.ie.util.ThreadUtils;
import com.zfoo.event.manager.EventBus;
import com.zfoo.net.config.manager.IConfigManager;
import com.zfoo.net.consumer.service.IConsumer;
import com.zfoo.net.core.AbstractServer;
import com.zfoo.net.core.tcp.TcpClient;
import com.zfoo.net.dispatcher.manager.IPacketDispatcher;
import com.zfoo.net.packet.service.IPacketService;
import com.zfoo.net.session.manager.ISessionManager;
import com.zfoo.net.task.TaskManager;
import com.zfoo.protocol.exception.ExceptionUtils;
import com.zfoo.protocol.util.ReflectionUtils;
import com.zfoo.scheduler.SchedulerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.core.Ordered;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;

/**
 * @author islandempty
 * @since 2021/7/16
 **/
public class NetContext implements ApplicationListener<ApplicationContextEvent>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(NetContext.class);

    private static NetContext instance;

    private ApplicationContext applicationContext;

    private IConfigManager configManager;

    private IPacketService packetService;

    private IPacketDispatcher packetDispatcher;

    private ISessionManager sessionManager;

    private IConsumer consumer;

    public static NetContext getNetContext() {
        return instance;
    }

    public static ApplicationContext getApplicationContext() {
        return instance.applicationContext;
    }

    public static IConfigManager getConfigManager() {
        return instance.configManager;
    }

    public static IPacketService getPacketService() {
        return instance.packetService;
    }

    public static ISessionManager getSessionManager() {
        return instance.sessionManager;
    }

    public static IPacketDispatcher getDispatcher() {
        return instance.packetDispatcher;
    }

    public static IConsumer getConsumer() {
        return instance.consumer;
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {

    }

    public synchronized static void shutdownBefore(){
        SchedulerContext.shutdown();
    }

    public static synchronized void shutdownAfter(){
        //关闭zookeeper客户端
        NetContext.getConfigManager().getRegistry().shutdown();

        //关闭所有session
        NetContext.getSessionManager().shutdown();

        //关闭所有客户端和服务器
        TcpClient.shutdown();
        AbstractServer.shutdownAllServers();

        //关闭TaskManager

        try {
            Field field = EventBus.class.getDeclaredField("executors");
            ReflectionUtils.makeAccessible(field);

            var executors = (ExecutorService[])ReflectionUtils.getField(field, TaskManager.class);
            for (ExecutorService service :executors){
                ThreadUtils.shutdown(service);
            }

        } catch (NoSuchFieldException e) {
            logger.error("Net thread pool failed shutdown: " + ExceptionUtils.getMessage(e));
            return;
        }

        logger.info("Net shutdown gracefully.");
    }
    @Override
    public int getOrder() {
        return 0;
    }
}

