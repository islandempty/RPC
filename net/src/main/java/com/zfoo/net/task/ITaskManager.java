package com.zfoo.net.task;

import com.zfoo.net.task.model.ReceiveTask;

import java.util.concurrent.ExecutorService;

public interface ITaskManager {

    void addTask(ReceiveTask task);

    ExecutorService getExecutorByConsistentHash(int hash);
}
