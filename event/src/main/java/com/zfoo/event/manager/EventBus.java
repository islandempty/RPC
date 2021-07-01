package com.zfoo.event.manager;

import com.ie.util.math.RandomUtils;
import com.zfoo.event.model.event.IEvent;
import com.zfoo.event.model.vo.IEventReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author islandempty
 * @since 2021/6/28
 **/
public class EventBus {

    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);

    //线程池大小,cpu核心线程数 * 2;
    private static final int EXECUTORS_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    private static final ExecutorService[] executors = new ExecutorService[EXECUTORS_SIZE];

    private static final Map<Class<? extends IEvent>, List<IEventReceiver>> receiverMap = new HashMap<>();

    static {
        for (int i = 0; i < executors.length; i++) {
            var nameThreadFactory = new EventThreadFactory(i + 1);
            executors[i] = Executors.newSingleThreadExecutor(nameThreadFactory);
        }
    }

    /**
     * 同步抛出一个事件，会在当前线程中运行
     *
     * @param event 需要抛出的事件
     */
    public static void syncSubmit(IEvent event){
        var list = receiverMap.get(event.getClass());
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        doSubmit(event,list);
    }

    /**
     * 异步抛出一个事件，事件不在同一个线程中处理
     *
     * @param event 需要抛出的事件
     */
    public static void asyncSubmit(IEvent event){
        var list = receiverMap.get(event.getClass());
        if (CollectionUtils.isEmpty(list)){
            return;
        }

        executors[Math.abs(event.threadId() % EXECUTORS_SIZE)].execute(new Runnable() {
            @Override
            public void run() {
                doSubmit(event,list);
            }
        });
    }

    /**
     * 随机获取一个线程池
     */
    public static Executor asyncExecute(){
        return executors[RandomUtils.randomInt(EXECUTORS_SIZE)];
    }

    private static void doSubmit(IEvent event,List<IEventReceiver> receiverList){
        for (var receiver:receiverList) {
            try {
                receiver.invoke(event);
            } catch (Exception e) {
                logger.error("eventBus未知exception异常", e);
            } catch (Throwable t) {
                logger.error("eventBus未知error异常", t);
            }
        }
    }
    public static void registerEventReceiver(Class<? extends IEvent> eventType,IEventReceiver eventReceiver){
        receiverMap.computeIfAbsent(eventType , it -> new LinkedList<>()).add(eventReceiver);
    }
}

