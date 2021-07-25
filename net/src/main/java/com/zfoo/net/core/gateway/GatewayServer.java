package com.zfoo.net.core.gateway;

import com.ie.util.net.HostAndPort;
import com.zfoo.net.core.AbstractServer;
import com.zfoo.net.handler.GatewayDispatcherHandler;
import com.zfoo.net.handler.codec.tcp.TcpCodeHandler;
import com.zfoo.net.handler.idle.ServerIdleHandler;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.lang.Nullable;

import java.util.function.BiFunction;

/**
 * @author islandempty
 * @since 2021/7/25
 **/
public class GatewayServer extends AbstractServer {

    private BiFunction<Session, IPacket, Boolean> packetFilter;

    public GatewayServer(HostAndPort host, @Nullable BiFunction<Session, IPacket, Boolean> packetFilter) {
        super(host);
        this.packetFilter = packetFilter;
    }

    @Override
    public ChannelInitializer<? extends Channel> channelChannelInitializer() {
        return new ChannelHandlerInitializer(packetFilter);
    }

    private static class ChannelHandlerInitializer extends ChannelInitializer<SocketChannel>{

        private BiFunction<Session, IPacket, Boolean> packetFilter;

        public ChannelHandlerInitializer(BiFunction<Session, IPacket, Boolean> packetFilter) {
            this.packetFilter = packetFilter;
        }

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new IdleStateHandler(0,0,180));
            socketChannel.pipeline().addLast(new ServerIdleHandler());
            socketChannel.pipeline().addLast(new TcpCodeHandler());
            socketChannel.pipeline().addLast(new GatewayDispatcherHandler(packetFilter));
        }
    }
}

