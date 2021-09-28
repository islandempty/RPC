package com.zfoo.net.config.model;

import com.zfoo.protocol.util.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author islandempty
 * @since 2021/7/17
 **/
public class RegistryConfig {

    private String center;
    private String user;
    private String password;
    private Map<String, String> address;

    public static RegistryConfig valueOf(String center, String user, String password, Map<String, String> addressMap) {
        RegistryConfig config = new RegistryConfig();
        config.center = center;
        config.user = user;
        config.password = password;
        config.address = addressMap;
        return config;
    }

    public boolean hasZookeeperAuthor() {
        return !(StringUtils.isBlank(user) || StringUtils.isBlank(password));
    }

    public String toZookeeperAuthor() {
        return user + StringUtils.COLON + password;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, String> getAddress() {
        return address;
    }

    public void setAddress(Map<String, String> address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegistryConfig that = (RegistryConfig) o;
        return Objects.equals(center, that.center) &&
                Objects.equals(user, that.user) &&
                Objects.equals(password, that.password) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, user, password, address);
    }
}

