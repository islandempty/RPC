package com.zfoo.net.task.model;

import java.util.concurrent.ExecutorService;

public interface ITaskDispatch {

    ExecutorService getExecutor(ReceiveTask receiveTask);
}
