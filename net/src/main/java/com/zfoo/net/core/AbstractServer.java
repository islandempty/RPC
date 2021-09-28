package com.zfoo.net.core;

import com.zfoo.protocol.util.IOUtils;
import com.zfoo.util.net.HostAndPort;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author islandempty
 * @since 2021/7/18
 **/
public abstract class AbstractServer implements IServer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractServer.class);

    // 所有的服务器都可以在这个列表中取到
    protected static final List<AbstractServer> allServers = new ArrayList<>(1);

    protected String hostAddress;
    protected int port;


    // 配置服务端nio线程组，服务端接受客户端连接
    private EventLoopGroup bossGroup;

    // SocketChannel的网络读写
    protected EventLoopGroup workerGroup;

    protected ChannelFuture channelFuture;

    protected Channel channel;

    public AbstractServer(HostAndPort host) {
        this.hostAddress = host.getHost();
        this.port = host.getPort();
    }

    public abstract ChannelInitializer<? extends Channel> channelChannelInitializer();

    @Override
    public void start() {
        doStart(channelChannelInitializer());
    }

    protected synchronized void doStart(ChannelInitializer<? extends Channel> channelChannelInitializer) {
        var cpuNum = Runtime.getRuntime().availableProcessors();
        bossGroup = Epoll.isAvailable()
                ? new EpollEventLoopGroup(Math.max(1, cpuNum / 4), new DefaultThreadFactory("netty-boss", true))
                : new NioEventLoopGroup(Math.max(1, cpuNum / 4), new DefaultThreadFactory("netty-boss", true));

        workerGroup = Epoll.isAvailable()
                ? new EpollEventLoopGroup(cpuNum * 2, new DefaultThreadFactory("netty-worker", true))
                : new NioEventLoopGroup(cpuNum * 2, new DefaultThreadFactory("netty-worker", true));

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(16 * IOUtils.BYTES_PER_KB, 16 * IOUtils.BYTES_PER_MB))
                .childHandler(channelChannelInitializer);
        // 绑定端口，同步等待成功
        // channelFuture = bootstrap.bind(hostAddress, port).sync();
        // 等待服务端监听端口关闭
        // channelFuture.channel().closeFuture().sync();


        // 异步
        channelFuture = bootstrap.bind(hostAddress, port);
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();

        allServers.add(this);

        logger.info("{} started at [{}:{}]", this.getClass().getSimpleName(), hostAddress, port);
    }


    @Override
    public synchronized void shutdown() {
        shutdownEventLoopGracefully(bossGroup);

        shutdownEventLoopGracefully(workerGroup);

        if (channelFuture != null) {
            try {
                channelFuture.channel().close().syncUninterruptibly();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }

        if (channel != null) {
            try {
                channel.close();
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    public synchronized static void shutdownEventLoopGracefully(EventExecutorGroup executor) {
        if (executor == null) {
            return;
        }
        try {
            if (executor.isShutdown() || executor.isTerminated()) {
                executor.shutdownGracefully();
            }
        } catch (Exception e) {
            logger.error("EventLoop Thread pool [{}] is failed to shutdown! ", executor, e);
            return;
        }
        logger.info("EventLoop Thread pool [{}] shuts down gracefully.", executor);
    }

    public synchronized static void shutdownAllServers() {
        allServers.forEach(it -> it.shutdown());
    }
}

