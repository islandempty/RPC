package com.zfoo.net.consumer.balancer;

import com.zfoo.protocol.exception.RunException;
import com.zfoo.util.math.ConsistentHash;
import com.zfoo.net.NetContext;
import com.zfoo.net.session.model.AttributeType;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.ProtocolManager;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.model.Pair;
import com.zfoo.protocol.registration.ProtocolModule;
import com.zfoo.protocol.util.StringUtils;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 一致性hash负载均衡器，同一个session总是发到同一提供者
 * <p>
 * 通过argument计算一致性hash
 *
 * @author islandempty
 * @since 2021/7/20
 **/
public class ConsistentHashConsumerLoadBalancer extends AbstractConsumerLoadBalancer {

    public static final ConsistentHashConsumerLoadBalancer INSTANCE = new ConsistentHashConsumerLoadBalancer();

    private volatile int lastClientSessionChangeId = 0;
    private static final Map<ProtocolModule, ConsistentHash<String, Long>> consistentHashMap = new ConcurrentHashMap<>();
    private static final int VIRTUAL_NODE_NUMS = 200;

    private ConsistentHashConsumerLoadBalancer() {
    }

    public static ConsistentHashConsumerLoadBalancer getInstance() {
        return INSTANCE;
    }

    /**
     * 通过argument的toString计算一致性hash，所以传入的argument一般要能代表唯一性，比如用户的id
     *
     * @param packet   请求包
     * @param argument 参数，一般要能代表唯一性，比如用户的id
     * @return 调用的session
     */
    @Override
    public Session loadBalancer(IPacket packet, Object argument) {
        if (argument == null) {
            return RandomConsumerLoadBalancer.getInstance().loadBalancer(packet, argument);
        }

        // 如果更新时间不匹配，则更新到最新的服务提供者
        var currentClientSessionChangeId = NetContext.getSessionManager().getClientSessionChangeId();
        if (currentClientSessionChangeId != lastClientSessionChangeId) {
            var modules = new HashSet<>(consistentHashMap.keySet());

            for (var module : modules) {
                updateModuleToConsistentHash(module);
            }

            lastClientSessionChangeId = currentClientSessionChangeId;
        }

        var module = ProtocolManager.moduleByProtocolId(packet.protocolId());
        var consistentHash = consistentHashMap.get(module);
        if (consistentHash == null) {
            consistentHash = updateModuleToConsistentHash(module);
        }
        if (consistentHash == null) {
            throw new RunException("一致性hash负载均衡[protocolId:{}]参数[argument:{}],没有服务提供者提供服务[module:{}]", packet.protocolId(), argument, module);
        }
        var sid = consistentHash.getRealNode(argument).getValue();
        return NetContext.getSessionManager().getClientSession(sid);

    }


    @Nullable
    private ConsistentHash<String, Long> updateModuleToConsistentHash(ProtocolModule module) {
        var sessionStringList = getSessionsByModule(module)
                .stream()
                .map(session -> new Pair<>(session.getAttribute(AttributeType.CONSUMER).toString(), session.getSid()))
                .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(sessionStringList) && !consistentHashMap.containsKey(module)) {
            consistentHashMap.remove(module);
            return null;
        }

        var consistentHash = new ConsistentHash<>(sessionStringList, VIRTUAL_NODE_NUMS);
        consistentHashMap.put(module, consistentHash);
        return consistentHash;
    }

}

