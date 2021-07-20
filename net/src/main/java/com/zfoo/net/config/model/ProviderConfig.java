package com.zfoo.net.config.model;

import com.ie.util.net.HostAndPort;
import com.ie.util.net.NetUtils;
import com.zfoo.protocol.registration.ProtocolModule;
import com.zfoo.protocol.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author islandempty
 * @since 2021/7/16
 **/
public class ProviderConfig {

    public static transient final int DEFAULT_PORT = 12400;

    /**
     * 对应于ITaskDispatch
     */
    private String dispatch;

    private String dispatchThread;

    private String address;

    private List<ProtocolModule> modules;

    public static ProviderConfig valueOf(String address, List<ProtocolModule> modules) {
        ProviderConfig config = new ProviderConfig();
        config.address = address;
        config.modules = modules;
        return config;
    }

    public HostAndPort localHostAndPortOrDefault(){
        if (StringUtils.isBlank(address)){
            var defaultHostAndPort = HostAndPort.valueOf(NetUtils.getLocalhostStr(), NetUtils.getAvailablePort(ProviderConfig.DEFAULT_PORT));
            this.address = defaultHostAndPort.toHostAndPortStr();
            return defaultHostAndPort;
        }
        return HostAndPort.valueOf(address);
    }

    public String getDispatch() {
        return dispatch;
    }

    public void setDispatch(String dispatch) {
        this.dispatch = dispatch;
    }

    public String getDispatchThread() {
        return dispatchThread;
    }

    public void setDispatchThread(String dispatchThread) {
        this.dispatchThread = dispatchThread;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        ProviderConfig that = (ProviderConfig) o;
        return Objects.equals(address, that.address) && Objects.equals(modules, that.modules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, modules);
    }
}

