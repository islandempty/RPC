package com.zfoo.net.task;

import com.zfoo.net.task.model.ITaskDispatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author islandempty
 * @since 2021/7/19
 **/
public final class TaskManager implements ITaskManager{

    private static final Logger logger = LoggerFactory.getLogger(TaskManager.class);

    private static final TaskManager INSTANCE = new TaskManager();


    private TaskManager() {
    }

    public static TaskManager getInstance() {
        return INSTANCE;
    }
    @Override
    public void addTask() {

    }
}

