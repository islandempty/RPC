package com.zfoo.event.manager;

import io.netty.util.concurrent.FastThreadLocalThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author islandempty
 * @since 2021/6/28
 **/
public class EventThreadFactory implements ThreadFactory {

    private static final Logger logger = LoggerFactory.getLogger(EventThreadFactory.class);

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public EventThreadFactory(int poolNumber) {
        var securityManager = System.getSecurityManager();
        group = (securityManager!=null)?securityManager.getThreadGroup():Thread.currentThread().getThreadGroup();
        namePrefix = "event-p" + poolNumber +"-t";
    }

    @Override
    public Thread newThread(Runnable runnable) {
        var thread = new FastThreadLocalThread(group, runnable, namePrefix + threadNumber.getAndIncrement(), 0);
        thread.setDaemon(false);
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.setUncaughtExceptionHandler((t,e)->logger.error(t.toString(),e));
        return thread;
    }
}

