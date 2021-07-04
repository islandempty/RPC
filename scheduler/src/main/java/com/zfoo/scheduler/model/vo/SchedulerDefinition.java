package com.zfoo.scheduler.model.vo;

import com.zfoo.protocol.util.ReflectionUtils;
import com.zfoo.scheduler.util.TimeUtils;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.springframework.scheduling.support.CronExpression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author islandempty
 * @since 2021/7/2
 **/
public class SchedulerDefinition {

    private CronExpression cronExpression;

    private IScheduler scheduler;

    /**
     * 触发时间戳，只要当前时间戳大于这个触发事件戳都视为可以触发
     */
    private long triggerTimestamp;

    public static SchedulerDefinition valueOf(String cron, Object bean, Method method)throws NoSuchMethodException, IllegalAccessException, InstantiationException, CannotCompileException, NotFoundException, InvocationTargetException {
        var schedulerDef = new SchedulerDefinition();
        var cronExpression = CronExpression.parse(cron);

        schedulerDef.cronExpression = cronExpression;
        //字节码增强，避免反射
        schedulerDef.scheduler = EnhanceUtils.createScheduler(ReflectScheduler.valueOf(bean,method));
        schedulerDef.triggerTimestamp = TimeUtils.getNextTimestampByCronExpression(cronExpression,TimeUtils.currentTimeMillis());
        ReflectionUtils.makeAccessible(method);
        return schedulerDef;
    }
    public static SchedulerDefinition valueOf(String cron , Runnable runnable){
        var schedulerDefinition = new SchedulerDefinition();
        var cronExpression = CronExpression.parse(cron);
        schedulerDefinition.cronExpression = cronExpression;
        schedulerDefinition.scheduler = RunnableScheduler.ValueOf(runnable);
        schedulerDefinition.triggerTimestamp = TimeUtils.getNextTimestampByCronExpression(cronExpression , TimeUtils.currentTimeMillis());
        return schedulerDefinition;
    }

    public CronExpression getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(CronExpression cronExpression) {
        this.cronExpression = cronExpression;
    }

    public IScheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(IScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public long getTriggerTimestamp() {
        return triggerTimestamp;
    }

    public void setTriggerTimestamp(long triggerTimestamp) {
        this.triggerTimestamp = triggerTimestamp;
    }
}

