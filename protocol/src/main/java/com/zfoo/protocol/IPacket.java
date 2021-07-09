package com.zfoo.protocol;

/**
 * 所有的协议类都必须实现这个接口
 *
 * @author islandempty
 * @since 2021/7/5
 **/
public interface IPacket {

    /**
     * 这个类的协议号
     * @return 协议号
     */
    short protocolId();
}

