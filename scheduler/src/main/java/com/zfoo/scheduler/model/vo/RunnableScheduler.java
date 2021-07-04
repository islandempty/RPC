package com.zfoo.scheduler.model.vo;

/**
 * @author islandempty
 * @since 2021/7/2
 **/
public class RunnableScheduler implements IScheduler{
    private Runnable runnable;

    public static RunnableScheduler ValueOf(Runnable runnable){
        var scheduler = new RunnableScheduler();
        scheduler.runnable = runnable;
        return scheduler;
    }
    @Override
    public void invoke() {
        runnable.run();
    }
}

