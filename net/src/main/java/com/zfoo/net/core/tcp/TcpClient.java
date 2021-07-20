package com.zfoo.net.core.tcp;

import com.ie.util.net.HostAndPort;
import com.zfoo.net.core.AbstractClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author islandempty
 * @since 2021/7/19
 **/
public class TcpClient extends AbstractClient {

    public TcpClient(HostAndPort host) {
        super(host);
    }


    @Override
    public ChannelInitializer<? extends Channel> channelChannelInitializer() {
        return null;
    }

    private static class ChannelHandlerInitializer extends ChannelInitializer<SocketChannel>{
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            //TODO
        }
    }
}

