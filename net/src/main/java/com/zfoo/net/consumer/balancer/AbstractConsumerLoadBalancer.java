package com.zfoo.net.consumer.balancer;

import com.zfoo.net.NetContext;
import com.zfoo.net.consumer.registry.RegisterVO;
import com.zfoo.net.session.model.AttributeType;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.ProtocolManager;
import com.zfoo.protocol.registration.ProtocolModule;
import com.zfoo.protocol.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author islandempty
 * @since 2021/7/19
 **/
public abstract class AbstractConsumerLoadBalancer implements IConsumerLoadBalancer{

    public static AbstractConsumerLoadBalancer valueOf(String loadBalancer){
        AbstractConsumerLoadBalancer balancer;
        switch (loadBalancer) {
            case "random":
                balancer = RandomConsumerLoadBalancer.getInstance();
                break;
            case "consistent-hash":
                balancer = ConsistentHashConsumerLoadBalancer.getInstance();
                break;
            case "shortest-time":
                balancer = ShortestTimeConsumerLoadBalancer.getInstance();
                break;
            default:
                throw new RuntimeException(StringUtils.format("无法识别负载均衡器[{}]", loadBalancer));
        }
        return balancer;
    }

    public List<Session> getSessionByPacket(IPacket packet){
        return getSessionByModule(ProtocolManager.moduleByProtocolId(packet.protocolId()));
    }

    public List<Session> getSessionByModule(ProtocolModule module){
        var clientSessionMap = NetContext.getSessionManager().getClientSessionMap();
        var sessions = clientSessionMap.values().stream()
                .filter(it ->{
                    var attribute = it.getAttribute(AttributeType.CONSUMER);
                    if (Objects.nonNull(attribute)){
                        var registerVo = (RegisterVO) attribute;
                        if (Objects.nonNull(registerVo.getProviderConfig())&&registerVo.getProviderConfig().getModules().contains(module)){
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }).collect(Collectors.toList());
        return sessions;
    }

    public boolean sessionHasModule(Session session, IPacket packet){
        var attribute = session.getAttribute(AttributeType.CONSUMER);
        if (Objects.isNull(attribute)){
            return false;
        }

        var registerVO = (RegisterVO) attribute;
        if (Objects.isNull(registerVO.getProviderConfig())){
            return false;
        }

        var module = ProtocolManager.moduleByProtocolId(packet.protocolId());
        return registerVO.getProviderConfig().getModules().contains(module);
    }
}

