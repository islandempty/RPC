package com.zfoo.net.config.model;

import com.zfoo.protocol.registration.ProtocolModule;

import java.util.List;
import java.util.Objects;

/**
 * @author islandempty
 * @since 2021/7/16
 **/
public class ConsumerConfig {

    private String loadBalancer;

    private List<ProtocolModule> modules;

    public static ConsumerConfig valueOf(String loadBalancer, List<ProtocolModule> modules) {
        ConsumerConfig config = new ConsumerConfig();
        config.loadBalancer = loadBalancer;
        config.modules = modules;
        return config;
    }

    public static ConsumerConfig valueOf(List<ProtocolModule> modules) {
        ConsumerConfig config = new ConsumerConfig();
        config.modules = modules;
        return config;
    }

    public String getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(String loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public List<ProtocolModule> getModules() {
        return modules;
    }

    public void setModules(List<ProtocolModule> modules) {
        this.modules = modules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConsumerConfig that = (ConsumerConfig) o;
        return Objects.equals(modules, that.modules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modules);
    }
}

