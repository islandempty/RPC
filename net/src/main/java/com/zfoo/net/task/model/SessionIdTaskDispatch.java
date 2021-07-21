package com.zfoo.net.task.model;

import com.ie.util.math.HashUtils;
import com.zfoo.net.task.TaskManager;

import java.util.concurrent.ExecutorService;

/**
 * @author islandempty
 * @since 2021/7/21
 **/
public class SessionIdTaskDispatch extends AbstractTaskDispatch{

    private static final SessionIdTaskDispatch INSTANCE = new SessionIdTaskDispatch();

    public static SessionIdTaskDispatch getInstance() {
        return INSTANCE;
    }

    @Override
    public ExecutorService getExecutor(ReceiveTask receiveTask) {
        var session = receiveTask.getSession();
        return TaskManager.getInstance().getExecutorByConsistentHash(HashUtils.fnvHash(session.getSid()));
    }
}

