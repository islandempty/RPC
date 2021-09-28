/*
 * Copyright (C) 2020 The zfoo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.net.core.websocket;

import com.zfoo.net.core.AbstractClient;
import com.zfoo.net.handler.ClientDispatcherHandler;
import com.zfoo.net.handler.codec.websocket.WebSocketCodecHandler;
import com.zfoo.protocol.util.IOUtils;
import com.zfoo.util.net.HostAndPort;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/*
 @islandempty
 */
public class WebsocketClient extends AbstractClient {

    private WebSocketClientProtocolConfig webSocketClientProtocolConfig;

    public WebsocketClient(HostAndPort host, WebSocketClientProtocolConfig webSocketClientProtocolConfig) {
        super(host);
        this.webSocketClientProtocolConfig = webSocketClientProtocolConfig;
    }

    @Override
    public ChannelInitializer<? extends Channel> channelChannelInitializer() {
        return new ChannelHandlerInitializer();
    }


    public class ChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        public void initChannel(SocketChannel channel) {
            channel.pipeline().addLast(new HttpClientCodec(8 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_KB));
            channel.pipeline().addLast(new HttpObjectAggregator(16 * IOUtils.BYTES_PER_MB));
            channel.pipeline().addLast(new WebSocketClientProtocolHandler(webSocketClientProtocolConfig));
            channel.pipeline().addLast(new ChunkedWriteHandler());
            channel.pipeline().addLast(new WebSocketCodecHandler());
            channel.pipeline().addLast(new ClientDispatcherHandler());
        }
    }


}
