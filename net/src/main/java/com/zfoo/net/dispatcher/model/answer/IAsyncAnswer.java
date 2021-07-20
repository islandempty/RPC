package com.zfoo.net.dispatcher.model.answer;

import com.zfoo.protocol.IPacket;

import java.util.function.Consumer;

public interface IAsyncAnswer<T extends IPacket> {

    IAsyncAnswer<T> thenAccept(Consumer<T> consumer);

    /**
     * 接收到异步返回的消息，并处理这个消息，异步请求必须要调用这个方法
     */
    void whenComplete(Consumer<T> consumer);
}
