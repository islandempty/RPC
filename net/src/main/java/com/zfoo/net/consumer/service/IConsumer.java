package com.zfoo.net.consumer.service;

import com.zfoo.net.dispatcher.model.answer.AsyncAnswer;
import com.zfoo.net.dispatcher.model.answer.SyncAnswer;
import com.zfoo.protocol.IPacket;
import org.springframework.lang.Nullable;

public interface IConsumer {

    /**
     * 直接发送，不需要任何返回值
     *
     * @param packet   需要发送的包
     * @param argument 计算负载均衡的参数，比如用户的id
     */
    void send(IPacket packet, @Nullable Object argument);

    <T extends IPacket> SyncAnswer<T> syncAsk(IPacket packet, Class<T> answerClass, @Nullable Object argument) throws Exception;

    <T extends IPacket> AsyncAnswer<T> asyncAsk(IPacket packet, Class<T> answerClass, @Nullable Object argument);
 }
