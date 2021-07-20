package com.ie.util.net;

import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author islandempty
 * @since 2021/7/16
 **/
public class HostAndPort {

    private String host;
    private int port;

    public static HostAndPort valueOf(String host, int port) {
        HostAndPort hostAndPort = new HostAndPort();
        hostAndPort.host = host;
        hostAndPort.port = port;
        return hostAndPort;
    }

    public static HostAndPort valueOf(String hostAndPort){
        //trim 删除头尾空白符的字符串
        var split = hostAndPort.trim().split(StringUtils.COLON_REGEX);
        return valueOf(split[0].trim(),Integer.parseInt(split[1].trim()));
    }

    public static List<HostAndPort> toHostAndPortList(String hostAndPort){
        if (StringUtils.isEmpty(hostAndPort)){
            return Collections.emptyList();
        }

        var hostAndPortSplits = hostAndPort.split(StringUtils.COMMA_REGEX);
        var hostAndPorts = new ArrayList<HostAndPort>();
        for (var hap : hostAndPortSplits){
            hostAndPorts.add(valueOf(hap));
        }
        return hostAndPorts;
    }

    public static List<HostAndPort> toHostAndPortList(Collection<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        var hostAndPortList = new ArrayList<HostAndPort>();
        list.forEach(it -> hostAndPortList.addAll(toHostAndPortList(it)));
        return hostAndPortList;
    }

    public static String toHostAndPortListStr(Collection<HostAndPort> list) {
        var urlList = list.stream()
                .map(it -> it.toHostAndPortStr())
                .collect(Collectors.toList());
        return StringUtils.joinWith(StringUtils.COMMA, urlList.toArray());
    }


    public String toHostAndPortStr() {
        return StringUtils.format("{}:{}", this.host.trim(), this.port);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HostAndPort that = (HostAndPort) o;
        return port == that.port && Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }

    @Override
    public String toString() {
        return StringUtils.format("[{}]", toHostAndPortStr());
    }
}

