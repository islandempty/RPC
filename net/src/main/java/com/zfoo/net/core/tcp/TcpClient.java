package com.zfoo.net.core.tcp;

import com.zfoo.util.net.HostAndPort;
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
        return new ChannelHandlerInitializer();
    }


    private static class ChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel channel) {
            channel.pipeline().addLast(new IdleStateHandler(0, 0, 60));
            channel.pipeline().addLast(new ClientIdleHandler());
            channel.pipeline().addLast(new TcpCodeHandler());
            channel.pipeline().addLast(new ClientDispatcherHandler());
        }
    }


}
