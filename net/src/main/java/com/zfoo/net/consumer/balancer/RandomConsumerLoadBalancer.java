package com.zfoo.net.consumer.balancer;

import com.ie.util.math.RandomUtils;
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
public class RandomConsumerLoadBalancer extends AbstractConsumerLoadBalancer{

    private static final RandomConsumerLoadBalancer INSTANCE = new RandomConsumerLoadBalancer();

    private RandomConsumerLoadBalancer(){

    }

    public static RandomConsumerLoadBalancer getInstance(){
        return INSTANCE;
    }

    /**
     * 只有一致性hash会使用这个argument参数，如果在一致性hash没有传入argument默认使用随机负载均衡
     *
     * @param packet   请求包
     * @param argument 计算参数
     * @return 一个服务提供者的session
     */
    @Override
    public Session loadBalancer(IPacket packet, Object argument) {
        var module = ProtocolManager.moduleByProtocolId(packet.protocolId());
        var sessionByModule = getSessionByModule(module);

        if (sessionByModule.isEmpty()){
            throw new RunException("没有服务提供者提供服务[{}]", module);
        }
        return RandomUtils.randomEle(sessionByModule);
    }
}

