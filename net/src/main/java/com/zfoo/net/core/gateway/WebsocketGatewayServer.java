package com.zfoo.net.core.gateway;

import com.ie.util.net.HostAndPort;
import com.zfoo.net.core.AbstractServer;
import com.zfoo.net.handler.GatewayDispatcherHandler;
import com.zfoo.net.handler.codec.websocket.WebSocketCodecHandler;
import com.zfoo.net.handler.idle.ServerIdleHandler;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.lang.Nullable;

import java.util.function.BiFunction;

/**
 * @author islandempty
 * @since 2021/7/25
 **/
public class WebsocketGatewayServer extends AbstractServer {

    private BiFunction<Session, IPacket, Boolean> packetFilter;

    public WebsocketGatewayServer(HostAndPort host, @Nullable BiFunction<Session, IPacket, Boolean> packetFilter) {
        super(host);
        this.packetFilter = packetFilter;
    }

    @Override
    public ChannelInitializer<SocketChannel> channelChannelInitializer() {
        return new ChannelHandlerInitializer(packetFilter);
    }
    private static class ChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

        private BiFunction<Session, IPacket, Boolean> packetFilter;

        public ChannelHandlerInitializer(BiFunction<Session, IPacket, Boolean> packetFilter) {
            this.packetFilter = packetFilter;
        }

        @Override
        protected void initChannel(SocketChannel channel) {
            channel.pipeline().addLast(new IdleStateHandler(0, 0, 180));
            channel.pipeline().addLast(new ServerIdleHandler());

            channel.pipeline().addLast(new HttpServerCodec());
            channel.pipeline().addLast(new ChunkedWriteHandler());
            channel.pipeline().addLast(new HttpObjectAggregator(64 * 1024));
            channel.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket"));
            channel.pipeline().addLast(new WebSocketCodecHandler());
            channel.pipeline().addLast(new GatewayDispatcherHandler(packetFilter));
        }
    }

}

