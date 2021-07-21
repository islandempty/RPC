package com.zfoo.net.task.model;

import com.zfoo.net.packet.model.IPacketAttachment;
import com.zfoo.net.task.TaskManager;

import java.util.concurrent.ExecutorService;

/**
 * @author islandempty
 * @since 2021/7/21
 **/
public class ConsistentHashTaskDispatch extends AbstractTaskDispatch{

    private static ConsistentHashTaskDispatch INSTANCE = new ConsistentHashTaskDispatch();

    public static ConsistentHashTaskDispatch getInstance(){
        return INSTANCE;
    }

    @Override
    public ExecutorService getExecutor(ReceiveTask receiveTask) {
        var packetAttachment = receiveTask.getPacketAttachment();

        if (packetAttachment == null){
            return SessionIdTaskDispatch.getInstance().getExecutor(receiveTask);
        }
        return TaskManager.getInstance().getExecutorByConsistentHash(packetAttachment.executorConsistentHash());
    }
}

