package com.zfoo.net.config.model;

import java.util.Map;
import java.util.Objects;

/**
 * @author islandempty
 * @since 2021/7/16
 **/
public class MonitorConfig {
    private String center;
    private String user;
    private String password;
    private Map<String, String> addressMap;

    public static MonitorConfig valueOf(String center, String user, String password, Map<String, String> addressMap) {
        MonitorConfig config = new MonitorConfig();
        config.center = center;
        config.user = user;
        config.password = password;
        config.addressMap = addressMap;
        return config;
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

    public Map<String, String> getAddressMap() {
        return addressMap;
    }

    public void setAddressMap(Map<String, String> addressMap) {
        this.addressMap = addressMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MonitorConfig that = (MonitorConfig) o;
        return Objects.equals(center, that.center) &&
                Objects.equals(user, that.user) &&
                Objects.equals(password, that.password) &&
                Objects.equals(addressMap, that.addressMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, user, password, addressMap);
    }
}

