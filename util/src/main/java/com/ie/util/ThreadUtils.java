package com.ie.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author islandempty
 * @since 2021/6/29
 **/
public abstract class ThreadUtils {
    private static final int WAIT_TIME = 10;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static void shutdown(ExecutorService executor){

            try {
                //若关闭后所有任务都已完成，则返回true。
                // 注意除非首先调用shutdown或shutdownNow，否则isTerminated永不为true
                if (!executor.isTerminated()) {
                    //在先前提交的任务（就是run中跑的东西）被执行的时候，开始有序的关闭。
                    // 新的任务不会被执行。如果已关闭，则调用没有其他效果。该方法不会等待先前已提交的任务完全执行。
                    executor.shutdown();
                    //关闭执行器（executor）当等待超过设定时间时，会监测ExecutorService是否已经关闭
                    if (!executor.awaitTermination(WAIT_TIME, TIME_UNIT)) {
                        //先停止接收外部提交的任务，忽略队列里等待的任务，尝试将正在跑的任务interrupt中断，返回未执行的任务列表。
                        executor.shutdownNow();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    public static void shutdownForkJoinPool(){
        try {
            ForkJoinPool.commonPool().shutdown();

            if (ForkJoinPool.commonPool().awaitTermination(WAIT_TIME,TIME_UNIT)){
                ForkJoinPool.commonPool().shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用kill退出的方式，不能调用这个停止方法
     */
    public static void shutdownApplication(){
        new Thread(()->{
           System.exit(0);
        });
    }
}

