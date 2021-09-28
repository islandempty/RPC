package com.zfoo.net.core.websocket;

import com.zfoo.protocol.util.IOUtils;
import com.zfoo.util.net.HostAndPort;
import com.zfoo.net.core.AbstractServer;
import com.zfoo.net.handler.ServerDispatcherHandler;
import com.zfoo.net.handler.codec.websocket.WebSocketCodecHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author islandempty
 * @since 2021/7/25
 **/
public class WebsocketServer extends AbstractServer {

    public WebsocketServer(HostAndPort host) {
        super(host);
    }

    @Override
    public ChannelInitializer<SocketChannel> channelChannelInitializer() {
        return new ChannelHandlerInitializer();
    }


    public static class ChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        public void initChannel(SocketChannel channel) {
            // 编解码 http 请求
            channel.pipeline().addLast(new HttpServerCodec(8 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_KB));
            // 聚合解码 HttpRequest/HttpContent/LastHttpContent 到 FullHttpRequest
            // 保证接收的 Http 请求的完整性
            channel.pipeline().addLast(new HttpObjectAggregator(16 * IOUtils.BYTES_PER_MB));
            // 处理其他的 WebSocketFrame
            channel.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket"));
            // 写文件内容，支持异步发送大的码流，一般用于发送文件流
            channel.pipeline().addLast(new ChunkedWriteHandler());
            // 编解码WebSocketFrame二进制协议
            channel.pipeline().addLast(new WebSocketCodecHandler());
            channel.pipeline().addLast(new ServerDispatcherHandler());
        }
    }

}

