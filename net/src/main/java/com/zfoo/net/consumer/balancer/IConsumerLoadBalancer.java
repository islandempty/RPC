package com.zfoo.net.consumer.balancer;

import com.zfoo.net.packet.model.SignalPacketAttachment;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;
import org.springframework.lang.Nullable;

public interface IConsumerLoadBalancer {

    /**
     * 只有一致性hash会使用这个argument参数，如果在一致性hash没有传入argument默认使用随机负载均衡
     *
     * @param packet   请求包
     * @param argument 计算参数
     * @return 一个服务提供者的session
     */
    Session loadBalancer(IPacket packet, @Nullable Object argument);

    default void beforeLoadBalancer(Session session, IPacket packet, SignalPacketAttachment attachment) {
    }

    default void afterLoadBalancer(Session session, IPacket packet, SignalPacketAttachment attachment) {
    }

}
