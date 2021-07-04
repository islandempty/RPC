package com.zfoo.scheduler.manager;

import io.netty.util.concurrent.FastThreadLocalThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author islandempty
 * @since 2021/7/1
 **/
public class SchedulerThreadFactory implements ThreadFactory {

    public static final Logger logger = LoggerFactory.getLogger(SchedulerThreadFactory.class);

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public SchedulerThreadFactory(int poolNumber){
        var securityManager = System.getSecurityManager();
        group = (securityManager!=null)?securityManager.getThreadGroup():Thread.currentThread().getThreadGroup();
        namePrefix = "scheduler-p" +poolNumber +"-t";
    }

    @Override
    public Thread newThread(Runnable runnable) {
        var t = new FastThreadLocalThread(group, runnable, namePrefix + threadNumber.getAndIncrement(), 0);
        t.setDaemon(false);
        t.setPriority(Thread.NORM_PRIORITY);
        t.setUncaughtExceptionHandler((thread, e) -> logger.error(thread.toString(),e));
        return t;
    }
}

