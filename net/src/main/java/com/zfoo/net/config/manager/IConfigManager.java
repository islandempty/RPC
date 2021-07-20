package com.zfoo.net.config.manager;

import com.zfoo.net.config.model.NetConfig;
import com.zfoo.net.consumer.balancer.AbstractConsumerLoadBalancer;
import com.zfoo.net.consumer.registry.IRegistry;

public interface IConfigManager {

    NetConfig getLocalConfig();

    AbstractConsumerLoadBalancer consumerLoadBalancer();

    void initRegistry();

    IRegistry getRegistry();
}
