package com.zfoo.net.core.tcp;

import com.ie.util.net.HostAndPort;
import com.zfoo.net.core.AbstractClient;
import com.zfoo.net.handler.ClientDispatcherHandler;
import com.zfoo.net.handler.codec.tcp.TcpCodeHandler;
import com.zfoo.net.handler.idle.ClientIdleHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

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
            //心跳检测
            socketChannel.pipeline().addLast(new IdleStateHandler(0,0,60));
            socketChannel.pipeline().addLast(new ClientIdleHandler());
            socketChannel.pipeline().addLast(new TcpCodeHandler());
            socketChannel.pipeline().addLast(new ClientDispatcherHandler());
        }
    }
}

