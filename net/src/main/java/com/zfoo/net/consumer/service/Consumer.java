package com.zfoo.net.consumer.service;

import com.ie.util.math.HashUtils;
import com.ie.util.math.RandomUtils;
import com.zfoo.net.NetContext;
import com.zfoo.net.consumer.balancer.AbstractConsumerLoadBalancer;
import com.zfoo.net.dispatcher.manager.PacketDispatcher;
import com.zfoo.net.dispatcher.model.answer.AsyncAnswer;
import com.zfoo.net.dispatcher.model.answer.SyncAnswer;
import com.zfoo.net.dispatcher.model.exception.ErrorResponseException;
import com.zfoo.net.dispatcher.model.exception.NetTimeOutException;
import com.zfoo.net.dispatcher.model.exception.UnexpectedProtocolException;
import com.zfoo.net.packet.common.Error;
import com.zfoo.net.packet.model.NoAnswerAttachment;
import com.zfoo.net.packet.model.SignalPacketAttachment;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.protocol.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * 服务调度和负载均衡，两个关键点：摘除故障节点，负载均衡
 *
 * 在clientSession中选择一个可用的session，最终还是调用的IPacketDispatcherManager中的方法
 *
 * @author islandempty
 * @since 2021/7/25
 **/
public class Consumer implements IConsumer{

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    /**
     * 直接发送，不需要任何返回值
     *
     * @param packet   需要发送的包
     * @param argument 计算负载均衡的参数，比如用户的id
     */
    @Override
    public void send(IPacket packet, Object argument) {
        try {
            var loadBalancer = NetContext.getConfigManager().consumerLoadBalancer();
            var session = loadBalancer.loadBalancer(packet, argument);
            var executorConsistentHash = (argument == null)? RandomUtils.randomInt() : HashUtils.fnvHash(argument);
            NetContext.getDispatcher().send(session,packet, NoAnswerAttachment.valueOf(executorConsistentHash));
        } catch (Throwable t) {
            logger.error("consumer发送未知异常", t);
        }
    }

    @Override
    public <T extends IPacket> SyncAnswer<T> syncAsk(IPacket packet, Class<T> answerClass, Object argument) throws Exception {
        var loadBalancer = NetContext.getConfigManager().consumerLoadBalancer();
        var session = loadBalancer.loadBalancer(packet, argument);

        // 下面的代码逻辑同PacketDispatcher的syncAsk
        var clientAttachment = new SignalPacketAttachment();
        var executorConsistentHash = (argument == null) ?RandomUtils.randomInt() : HashUtils.fnvHash(argument);
        clientAttachment.setExecutorConsistentHash(executorConsistentHash);

        try {
            session.addClientSignalAttachment(clientAttachment);
            // load balancer之前调用
            loadBalancer.beforeLoadBalancer(session , packet , clientAttachment);

            NetContext.getDispatcher().send(session,packet,clientAttachment);

            IPacket responsePacket = clientAttachment.getResponseFuture().get(PacketDispatcher.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);

            if (responsePacket.protocolId() == Error.errorProtocolId()) {
                throw new ErrorResponseException((Error) responsePacket);
            }
            if (answerClass != null && answerClass != responsePacket.getClass()) {
                throw new UnexpectedProtocolException(StringUtils.format("client expect protocol:[{}], but found protocol:[{}]"
                        , answerClass, responsePacket.getClass().getName()));
            }

           var syncAnswer = new SyncAnswer<>((T) responsePacket, clientAttachment);

            // load balancer之后调用
            loadBalancer.afterLoadBalancer(session, packet, clientAttachment);
            return syncAnswer;
        } catch (TimeoutException e) {
            throw new NetTimeOutException(StringUtils.format("syncRequest timeout exception, ask:[{}], attachment:[{}]"
                    , JsonUtils.object2String(packet), JsonUtils.object2String(clientAttachment)));
        } finally {
            session.removeClientSignalAttachment(clientAttachment);
        }
    }

    @Override
    public <T extends IPacket> AsyncAnswer<T> asyncAsk(IPacket packet, Class<T> answerClass, Object argument) {
        var loadBalancer = NetContext.getConfigManager().consumerLoadBalancer();
        var session = loadBalancer.loadBalancer(packet, argument);
        var asyncAnswer = NetContext.getDispatcher().asyncAsk(session, packet, answerClass, argument);

        // load balancer之前调用
        loadBalancer.beforeLoadBalancer(session, packet, asyncAnswer.getFutureAttachment());

        // load balancer之后调用
        asyncAnswer.thenAccept(responsePacket -> loadBalancer.afterLoadBalancer(session, packet, asyncAnswer.getFutureAttachment()));
        return asyncAnswer;
    }
}

