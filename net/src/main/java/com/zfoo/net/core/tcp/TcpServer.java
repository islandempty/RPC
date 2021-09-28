package com.zfoo.net.core.tcp;


import com.zfoo.util.net.HostAndPort;
import com.zfoo.net.core.AbstractServer;
import com.zfoo.net.handler.ServerDispatcherHandler;
import com.zfoo.net.handler.codec.tcp.TcpCodeHandler;
import com.zfoo.net.handler.idle.ServerIdleHandler;
import com.zfoo.util.net.HostAndPort;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author islandempty
 * @since 2021/7/23
 **/
public class TcpServer extends AbstractServer {

    public TcpServer(HostAndPort host) {
        super(host);
    }

    @Override
    public ChannelInitializer<SocketChannel> channelChannelInitializer() {
        return new ChannelHandlerInitializer();
    }


    private static class ChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel channel) {
            channel.pipeline().addLast(new IdleStateHandler(0, 0, 180));
            channel.pipeline().addLast(new ServerIdleHandler());
            channel.pipeline().addLast(new TcpCodeHandler());
            channel.pipeline().addLast(new ServerDispatcherHandler());
        }
    }
}

