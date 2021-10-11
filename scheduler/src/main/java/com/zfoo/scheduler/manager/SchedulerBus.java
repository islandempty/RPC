package com.zfoo.scheduler.manager;

import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.scheduler.SchedulerContext;
import com.zfoo.scheduler.model.vo.SchedulerDefinition;
import com.zfoo.scheduler.timeWheelUtils.Timer;
import com.zfoo.scheduler.timeWheelUtils.TimerTask;
import com.zfoo.scheduler.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author islandempty
 * @since 2021/7/2
 **/
public abstract class SchedulerBus {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerBus.class);

    private static Timer timer = new Timer();

    public static final long TRIGGER_MILLIS_INTERVAL = TimeUtils.MILLIS_PER_SECOND;
    /**
     * scheduler默认只有一个单线程的线程池
     */
    public static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new SchedulerThreadFactory(1));

    static {
        executor.scheduleAtFixedRate(() -> {
            try {
                timer.advanceClock(10);
            } catch (Exception e) {
                logger.error("scheduler triggers an error.", e);
            }
        }, 0, TRIGGER_MILLIS_INTERVAL, TimeUnit.MILLISECONDS);
    }

    public static void registerScheduler(SchedulerDefinition scheduler) {
        var timerTask = new TimerTask(scheduler.getTriggerTimestamp(), () -> {
            scheduler.getScheduler().invoke();
            refreshTask(scheduler);
        });
        timer.addTask(timerTask);
    }


    /**
     * 不断执行的周期循环任务
     */
    public static void scheduleAtFixedRate(Runnable runnable, long period, TimeUnit unit) {
        if (SchedulerContext.isStop()) {
            return;
        }

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    logger.error("scheduleAtFixedRate未知exception异常", e);
                } catch (Throwable t) {
                    logger.error("scheduleAtFixedRate未知error异常", t);
                }
            }
        }, 0, period, unit);
    }


    /**
     * 固定延迟执行的任务
     */
    public static void schedule(Runnable runnable, long delay, TimeUnit unit) {
        if (SchedulerContext.isStop()) {
            return;
        }

        executor.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    logger.error("schedule未知exception异常", e);
                } catch (Throwable t) {
                    logger.error("schedule未知error异常", t);
                }
            }
        }, delay, unit);
    }

    public static void refreshTask(SchedulerDefinition schedulerDefinition) {
        var timestamp = TimeUtils.currentTimeMillis();
        var nextTriggerTimestamp = TimeUtils.getNextTimestampByCronExpression(schedulerDefinition.getCronExpression(), timestamp);
        schedulerDefinition.setTriggerTimestamp(nextTriggerTimestamp);
        var timerTask = new TimerTask(schedulerDefinition.getTriggerTimestamp(), () -> {
            schedulerDefinition.getScheduler().invoke();
            refreshTask(schedulerDefinition);
        });
        timer.addTask(timerTask);
    }
}

