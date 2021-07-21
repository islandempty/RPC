package com.zfoo.net.consumer.balancer;

import com.zfoo.net.packet.model.SignalPacketAttachment;
import com.zfoo.net.session.model.AttributeType;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.ProtocolManager;
import com.zfoo.protocol.exception.RunException;
import com.zfoo.protocol.registration.ProtocolModule;
import com.zfoo.scheduler.util.TimeUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 最少时间调用负载均衡器，优先选择调用时间最短的session
 *
 * @author islandempty
 * @since 2021/7/21
 **/
public class ShortestTimeConsumerLoadBalancer extends AbstractConsumerLoadBalancer{

    private static final ShortestTimeConsumerLoadBalancer INSTANCE = new ShortestTimeConsumerLoadBalancer();

    private ShortestTimeConsumerLoadBalancer(){

    }

    public static ShortestTimeConsumerLoadBalancer getInstance(){
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
        var sessions = getSessionByModule(module);

        if (sessions.isEmpty()){
            throw new RunException("没有服务提供者提供服务[{}]", module);
        }

        var sortedSessions = sessions.stream()
                .sorted((a, b) -> {
                    var aMap = (Map<Short, Long>) a.getAttribute(AttributeType.RESPONSE_TIME);
                    var bMap = (Map<Short, Long>) b.getAttribute(AttributeType.RESPONSE_TIME);
                    if (aMap == null) {
                        return -1;
                    } else if (bMap == null) {
                        return 1;
                    } else {
                        var aTime = aMap.get(packet.protocolId());
                        var bTime = bMap.get(packet.protocolId());
                        if (aTime == null) {
                            return -1;
                        } else if (bTime == null) {
                            return 1;
                        } else {
                            return (aTime > bTime) ? 1 : -1;
                        }
                    }
                }).findFirst();
        return sortedSessions.get();
    }

    @Override
    public void beforeLoadBalancer(Session session, IPacket packet, SignalPacketAttachment attachment) {
        // 因为要通过最短响应时间来路由分发消息，这里使用更精确的时间
        attachment.setTimestamp(TimeUtils.currentTimeMillis());
    }

    @Override
    public void afterLoadBalancer(Session session, IPacket packet, SignalPacketAttachment attachment) {
        var map = (Map<Short, Long>) session.getAttribute(AttributeType.RESPONSE_TIME);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            session.putAttribute(AttributeType.RESPONSE_TIME, map);
        }
        map.put(packet.protocolId(), TimeUtils.currentTimeMillis() - attachment.getTimestamp());
    }
}

