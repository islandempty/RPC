package com.zfoo.net.config.manager;

import com.zfoo.net.config.model.NetConfig;
import com.zfoo.net.config.model.ProviderConfig;
import com.zfoo.net.consumer.balancer.AbstractConsumerLoadBalancer;
import com.zfoo.net.consumer.registry.IRegistry;
import com.zfoo.net.consumer.registry.ZookeeperRegistry;
import com.zfoo.protocol.ProtocolManager;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.registration.ProtocolModule;
import com.zfoo.protocol.util.AssertionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author islandempty
 * @since 2021/7/22
 **/
public class ConfigManager implements IConfigManager{

    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    /**
     * 本地设置
     */
    private NetConfig localConfig;

    private AbstractConsumerLoadBalancer consumerLoadBalancer;

    /**
     * 注册中心
     */
    private IRegistry registry;

    @Override
    public NetConfig getLocalConfig() {
        return localConfig;
    }

    public void setLocalConfig(NetConfig localConfig) {
        this.localConfig = localConfig;
    }

    @Override
    public AbstractConsumerLoadBalancer consumerLoadBalancer() {
        return consumerLoadBalancer;
    }

    @Override
    public void initRegistry() {
        // 通过protocol，写入provider的module的id和version
        var providerConfig = localConfig.getProviderConfig();
        if (Objects.nonNull(providerConfig) && CollectionUtils.isNotEmpty(providerConfig.getModules())){
            var protocolModules = new ArrayList<ProtocolModule>(providerConfig.getModules().size());
            for (var providerModule : providerConfig.getModules()){
                var module = ProtocolManager.moduleByModuleName(providerModule.getName());
                AssertionUtils.isTrue(module != null, "服务提供者[name:{}]在协议文件中不存在", providerModule.getName());
                protocolModules.add(module);
            }
            providerConfig.setModules(protocolModules);
        }

        // 通过protocol，写入consumer的module的id和version
        var consumerConfig = localConfig.getConsumerConfig();
        if (Objects.nonNull(consumerConfig) && CollectionUtils.isNotEmpty(consumerConfig.getModules())) {
            var consumerModules = new ArrayList<ProtocolModule>(consumerConfig.getModules().size());
            for (var providerModule : consumerConfig.getModules()) {
                var module = ProtocolManager.moduleByModuleName(providerModule.getName());
                AssertionUtils.isTrue(module != null, "消费者[name:{}]在协议文件中不存在", providerModule.getName());
                consumerModules.add(module);
            }
            consumerConfig.setModules(consumerModules);
            consumerLoadBalancer = AbstractConsumerLoadBalancer.valueOf(consumerConfig.getLoadBalancer());
        }

        registry = new ZookeeperRegistry();
        registry.start();
    }

    @Override
    public IRegistry getRegistry() {
        return registry;
    }
}

