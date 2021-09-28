package com.zfoo.net.core.udp;

import com.zfoo.util.net.HostAndPort;
import com.zfoo.net.core.AbstractServer;
import com.zfoo.net.handler.ServerDispatcherHandler;
import com.zfoo.net.handler.codec.udp.UdpCodecHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author islandempty
 * @since 2021/7/25
 **/
public class UdpServer extends AbstractServer {

    private static final Logger logger = LoggerFactory.getLogger(UdpServer.class);

    public UdpServer(HostAndPort host) {
        super(host);
    }

    @Override
    public void start() {
        var cpuNum = Runtime.getRuntime().availableProcessors();

        // 配置服务端nio线程组
        workerGroup = Epoll.isAvailable()
                ? new EpollEventLoopGroup(cpuNum * 2, new DefaultThreadFactory("netty-worker", true))
                : new NioEventLoopGroup(cpuNum * 2, new DefaultThreadFactory("netty-worker", true));

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(Epoll.isAvailable() ? EpollDatagramChannel.class : NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(channelChannelInitializer());

        // 异步
        channelFuture = bootstrap.bind(hostAddress, port);
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();

        allServers.add(this);

        logger.info("{} started at [{}:{}]", this.getClass().getSimpleName(), hostAddress, port);
    }

    @Override
    public ChannelInitializer<Channel> channelChannelInitializer() {
        return new ChannelHandlerInitializer();
    }


    private static class ChannelHandlerInitializer extends ChannelInitializer<Channel> {
        @Override
        protected void initChannel(Channel channel) {
            channel.pipeline().addLast(new UdpCodecHandler());
            channel.pipeline().addLast(new ServerDispatcherHandler());
        }
    }
}

