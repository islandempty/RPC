package com.zfoo.net.schema;

import com.zfoo.net.dispatcher.manager.PacketBus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author islandempty
 * @since 2021/7/25
 **/
public class NetProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        PacketBus.registerPacketReceiverDefinition(bean);
        return bean;
    }
}

