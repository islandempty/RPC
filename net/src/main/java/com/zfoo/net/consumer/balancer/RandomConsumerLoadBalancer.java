package com.zfoo.net.consumer.balancer;

import com.zfoo.util.math.RandomUtils;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.ProtocolManager;
import com.zfoo.protocol.exception.RunException;
import com.zfoo.protocol.registration.ProtocolModule;

import java.util.List;

/**
 * 随机负载均衡器，任选服务提供者的其中之一
 *
 * @author islandempty
 * @since 2021/7/21
 **/
public class RandomConsumerLoadBalancer extends AbstractConsumerLoadBalancer {

    private static final RandomConsumerLoadBalancer INSTANCE = new RandomConsumerLoadBalancer();

    private RandomConsumerLoadBalancer() {
    }

    public static RandomConsumerLoadBalancer getInstance() {
        return INSTANCE;
    }

    @Override
    public Session loadBalancer(IPacket packet, Object argument) {
        var module = ProtocolManager.moduleByProtocolId(packet.protocolId());
        var sessions = getSessionsByModule(module);

        if (sessions.isEmpty()) {
            throw new RunException("一致性hash负载均衡[protocolId:{}]参数[argument:{}],没有服务提供者提供服务[module:{}]", packet.protocolId(), argument, module);
        }

        return RandomUtils.randomEle(sessions);
    }
}

