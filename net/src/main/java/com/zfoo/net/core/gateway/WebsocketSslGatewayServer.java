package com.zfoo.net.core.gateway;

import com.ie.util.net.HostAndPort;
import com.zfoo.net.core.AbstractServer;
import com.zfoo.net.handler.GatewayDispatcherHandler;
import com.zfoo.net.handler.codec.websocket.WebSocketCodecHandler;
import com.zfoo.net.handler.idle.ServerIdleHandler;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.exception.ExceptionUtils;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.InputStream;
import java.util.function.BiFunction;

/**
 * @author islandempty
 * @since 2021/7/25
 **/
public class WebsocketSslGatewayServer extends AbstractServer {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketSslGatewayServer.class);

    private SslContext sslContext;

    private BiFunction<Session, IPacket, Boolean> packetFilter;

    public WebsocketSslGatewayServer(HostAndPort host, InputStream pem, InputStream key, BiFunction<Session, IPacket, Boolean> packetFilter) {
        super(host);
        try {
            this.sslContext = SslContextBuilder.forServer(pem, key).build();
        } catch (SSLException e) {
            logger.error(ExceptionUtils.getMessage(e));
        }
        this.packetFilter = packetFilter;
    }

    @Override
    public ChannelInitializer<SocketChannel> channelChannelInitializer() {
        return new ChannelHandlerInitializer(sslContext, packetFilter);
    }


    private static class ChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

        private SslContext sslContext;
        private BiFunction<Session, IPacket, Boolean> packetFilter;

        public ChannelHandlerInitializer(SslContext sslContext, BiFunction<Session, IPacket, Boolean> packetFilter) {
            this.sslContext = sslContext;
            this.packetFilter = packetFilter;
        }

        @Override
        protected void initChannel(SocketChannel channel) {
            channel.pipeline().addLast(new IdleStateHandler(0, 0, 180));
            channel.pipeline().addLast(new ServerIdleHandler());

            channel.pipeline().addLast(sslContext.newHandler(channel.alloc()));
            channel.pipeline().addLast(new HttpServerCodec());
            channel.pipeline().addLast(new ChunkedWriteHandler());
            channel.pipeline().addLast(new HttpObjectAggregator(64 * 1024));
            channel.pipeline().addLast(new WebSocketServerProtocolHandler("/"));
            channel.pipeline().addLast(new WebSocketCodecHandler());
            channel.pipeline().addLast(new GatewayDispatcherHandler(packetFilter));
        }
    }
}

