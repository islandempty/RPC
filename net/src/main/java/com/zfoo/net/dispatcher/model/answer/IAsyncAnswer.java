package com.zfoo.net.dispatcher.model.answer;

import com.zfoo.net.task.model.SafeRunnable;
import com.zfoo.protocol.IPacket;

import java.util.function.Consumer;

public interface IAsyncAnswer<T extends IPacket> {

    IAsyncAnswer<T> thenAccept(Consumer<T> consumer);

    /**
     * 接收到异步返回的消息，并处理这个消息，异步请求必须要调用这个方法
     */
    void whenComplete(Consumer<T> consumer);

    /**
     * 没有执行成功的回调的方法
     */
    IAsyncAnswer<T> notComplete(SafeRunnable notCompleteCallback);

}
