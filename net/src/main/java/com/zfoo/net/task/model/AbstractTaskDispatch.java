package com.zfoo.net.task.model;

import com.zfoo.protocol.util.StringUtils;

/**
 * @author islandempty
 * @since 2021/7/16
 **/
public abstract class AbstractTaskDispatch implements ITaskDispatch{

    public static ITaskDispatch valueOf(String taskDispatchName){
        switch (taskDispatchName) {
            case "random":
                return new RandomTaskDispatch();
            case "sessionId":
                return new SessionIdTaskDispatch();
            case "consistent-hash":
                return new ConsistentHashTaskDispatch();
            default:
                throw new RuntimeException(StringUtils.format("没有找到对应的taskDispatch[{}]", taskDispatchName));
        }
    }
}

