package com.zfoo.net.config.model;

import com.zfoo.net.consumer.registry.RegisterVO;
import com.zfoo.protocol.generate.GenerateOperation;

import java.util.Objects;

/**
 * @author islandempty
 * @since 2021/7/19
 **/
public class NetConfig {
    private String id;
    private String protocolLocation;

    /**
     * 协议生成属性变量对应于{@link GenerateOperation}
     */
    private boolean foldProtocol;
    private String protocolParam;
    private boolean generateJsProtocol;
    private boolean generateCsProtocol;
    private boolean generateLuaProtocol;

    private RegistryConfig registry;
    private MonitorConfig monitor;
    private HostConfig host;

    private ProviderConfig provider;
    private ConsumerConfig consumer;


    public RegisterVO toLocalRegisterVO() {
        return RegisterVO.valueOf(id, provider, consumer);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProtocolLocation() {
        return protocolLocation;
    }

    public void setProtocolLocation(String protocolLocation) {
        this.protocolLocation = protocolLocation;
    }

    public boolean isFoldProtocol() {
        return foldProtocol;
    }

    public void setFoldProtocol(boolean foldProtocol) {
        this.foldProtocol = foldProtocol;
    }

    public String getProtocolParam() {
        return protocolParam;
    }

    public void setProtocolParam(String protocolParam) {
        this.protocolParam = protocolParam;
    }

    public boolean isGenerateJsProtocol() {
        return generateJsProtocol;
    }

    public void setGenerateJsProtocol(boolean generateJsProtocol) {
        this.generateJsProtocol = generateJsProtocol;
    }

    public boolean isGenerateCsProtocol() {
        return generateCsProtocol;
    }

    public void setGenerateCsProtocol(boolean generateCsProtocol) {
        this.generateCsProtocol = generateCsProtocol;
    }

    public boolean isGenerateLuaProtocol() {
        return generateLuaProtocol;
    }

    public void setGenerateLuaProtocol(boolean generateLuaProtocol) {
        this.generateLuaProtocol = generateLuaProtocol;
    }

    public RegistryConfig getRegistry() {
        return registry;
    }

    public void setRegistry(RegistryConfig registry) {
        this.registry = registry;
    }

    public MonitorConfig getMonitor() {
        return monitor;
    }

    public void setMonitor(MonitorConfig monitor) {
        this.monitor = monitor;
    }

    public HostConfig getHost() {
        return host;
    }

    public void setHost(HostConfig host) {
        this.host = host;
    }

    public ProviderConfig getProvider() {
        return provider;
    }

    public void setProvider(ProviderConfig provider) {
        this.provider = provider;
    }

    public ConsumerConfig getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerConfig consumer) {
        this.consumer = consumer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NetConfig netConfig = (NetConfig) o;
        return generateJsProtocol == netConfig.generateJsProtocol &&
                Objects.equals(id, netConfig.id) &&
                Objects.equals(protocolLocation, netConfig.protocolLocation) &&
                Objects.equals(registry, netConfig.registry) &&
                Objects.equals(monitor, netConfig.monitor) &&
                Objects.equals(host, netConfig.host) &&
                Objects.equals(provider, netConfig.provider) &&
                Objects.equals(consumer, netConfig.consumer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, protocolLocation, generateJsProtocol, registry, monitor, host, provider, consumer);
    }
}

