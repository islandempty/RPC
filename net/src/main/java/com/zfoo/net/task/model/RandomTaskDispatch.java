package com.zfoo.net.task.model;

import com.zfoo.net.packet.model.IPacketAttachment;
import com.zfoo.net.task.TaskManager;

import java.util.concurrent.ExecutorService;

/**
 * @author islandempty
 * @since 2021/7/21
 **/
public class RandomTaskDispatch extends AbstractTaskDispatch{

    private static final RandomTaskDispatch INSTANCE = new RandomTaskDispatch();

    public static RandomTaskDispatch getInstance() {
        return INSTANCE;
    }

    @Override
    public ExecutorService getExecutor(ReceiveTask receiveTask) {
        return TaskManager.getInstance().getExecutorByConsistentHash(-1);
    }
}

