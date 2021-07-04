package com.zfoo.scheduler.manager;

import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.scheduler.SchedulerContext;
import com.zfoo.scheduler.model.vo.SchedulerDefinition;
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

    private static final List<SchedulerDefinition> schedulerDefList = new CopyOnWriteArrayList<>();

    /**
     * 上次trigger触发时间
     */
    private static long lastTriggerTimestamp =0;

    /**
     * 在scheduler中，最小的triggerTimestamp
     */
    private static long minSchedulerTriggerTimestamp = 0;

    public static final long TRIGGER_MILLIS_INTERVAL = TimeUtils.MILLIS_PER_SECOND;
    /**
     * scheduler默认只有一个单线程的线程池
     * ScheduledExecutorService执行定时任务
     */
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new SchedulerThreadFactory(1));

    static {
        //执行线程   初始化延时  两次开始执行最小间隔时间  计时单位
        executor.scheduleAtFixedRate(() -> {
            try {
                triggerPerSecond();
            } catch (Exception e){
                logger.error("scheduler triggers an error.", e);
            }
        },3 * TimeUtils.MILLIS_PER_SECOND , TRIGGER_MILLIS_INTERVAL , TimeUnit.MILLISECONDS);
    }

    private static long minSchedulerTriggerTimestamp(){
        var minSchedulerOptional = schedulerDefList.stream().min(Comparator.comparingLong(schedulerDef -> schedulerDef.getTriggerTimestamp()));
        if (minSchedulerOptional.isPresent()){
            return minSchedulerOptional.get().getTriggerTimestamp();
        }else {
            logger.error("schedulerDefList:[{}] has no minSchedulerTriggerTimestamp to return. ", JsonUtils.object2String(schedulerDefList));
            return 0;
        }
    }

    /**
     * 每一秒执行一次，如果这个任务执行时间过长超过，比如10秒，执行完成后，不会再执行10次
     */
    private static void triggerPerSecond(){
        var timestamp = TimeUtils.currentTimeMillis();

        if (CollectionUtils.isEmpty(schedulerDefList)){
            return;
        }
        /**
         * 有人向前调整过机器时间，重新计算scheduler里的triggerTimestamp
         */
        if (timestamp < lastTriggerTimestamp){
            for (SchedulerDefinition schedulerDef:schedulerDefList){
                var nextTriggerTimestamp = TimeUtils.getNextTimestampByCronExpression(schedulerDef.getCronExpression(), timestamp);
                schedulerDef.setTriggerTimestamp(nextTriggerTimestamp);
            }
            minSchedulerTriggerTimestamp =minSchedulerTriggerTimestamp();
        }
        // diff > 0, 没有人调整时间或者有人向后调整过机器时间，可以忽略，因为向后调整时间时间戳一定会大于triggerTimestamp，所以一定会触发
        lastTriggerTimestamp = timestamp;

        // 如果minSchedulerTriggerTimestamp大于timestamp，说明没有可执行的scheduler
        if (timestamp <minSchedulerTriggerTimestamp){
            return;
        }

        for (var schedulerDef  : schedulerDefList){
            if (timestamp >= schedulerDef.getTriggerTimestamp()){
                //到达触发时间，则执行runnable方法
                schedulerDef.getScheduler().invoke();
                //重新设置一下下一次触发的时间
                var nextTriggerTimestamp = TimeUtils.getNextTimestampByCronExpression(schedulerDef.getCronExpression(), timestamp);
                schedulerDef.setTriggerTimestamp(nextTriggerTimestamp);
            }
        }
        minSchedulerTriggerTimestamp = minSchedulerTriggerTimestamp();
    }

    public static void registerScheduler(SchedulerDefinition schedulerDefinition){
        schedulerDefList.add(schedulerDefinition);
        minSchedulerTriggerTimestamp =minSchedulerTriggerTimestamp();
    }

    /**
     *不断执行周期任务
     * @param runnable
     * @param period 两次开始执行最小间隔时间
     * @param unit 计时单位
     */
    public static void schedulerAtFixedRate(Runnable runnable,long period ,TimeUnit unit){
        if (SchedulerContext.isStop()){
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
        },0,period,unit);
    }
    public static void scheduler(Runnable runnable,long delay ,TimeUnit unit){
        if (SchedulerContext.isStop()){
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
        },delay,unit);
    }

    /**
     * cron表达式执行的任务
     */
    public void schedulerCron(Runnable runnable, String cron){
        if (SchedulerContext.isStop()){
            return;
        }

        schedulerDefList.add(SchedulerDefinition.valueOf(cron,runnable));
    }
}

