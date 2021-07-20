package com.zfoo.net.core;

import com.ie.util.net.HostAndPort;
import com.zfoo.net.NetContext;
import com.zfoo.net.handler.BaseDispatcherHandler;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.exception.ExceptionUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author islandempty
 * @since 2021/7/19
 **/
public abstract class AbstractClient implements IClient{

    protected static final Logger logger = LoggerFactory.getLogger(AbstractClient.class);

    protected static final EventLoopGroup nioEventLoopGroup = Epoll.isAvailable()
            ? new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors()+1,new DefaultThreadFactory("netty-client",true))
            : new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() + 1, new DefaultThreadFactory("netty-client", true));

    protected String hostAddress;
    protected int port;

    protected Bootstrap bootstrap;

    public AbstractClient(HostAndPort host) {
        this.hostAddress = host.getHost();
        this.port = host.getPort();
    }

    public abstract ChannelInitializer<? extends Channel> channelChannelInitializer();

    @Override
    public synchronized Session start() {
        return doStart(channelChannelInitializer());
    }

    private synchronized Session doStart(ChannelInitializer<? extends  Channel> channelInitializer){
       this.bootstrap = new Bootstrap();
       this.bootstrap.group(nioEventLoopGroup)
               .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
               .option(ChannelOption.TCP_NODELAY,true)
               .handler(channelChannelInitializer());

        var channelFuture = bootstrap.connect(hostAddress, port);
        channelFuture.syncUninterruptibly();

        if (channelFuture.isSuccess()){
            var channel = channelFuture.channel();
            var session = BaseDispatcherHandler.initChannel(channel);
            NetContext.getSessionManager().addClientSession(session);
            logger.info("TcpClient started at [{}]", channel.localAddress());
            return session;
        }else if (channelFuture.cause() != null) {
            logger.error(ExceptionUtils.getMessage(channelFuture.cause()));
        } else {
            logger.error("启动客户端[client:{}]未知错误", this);
        }
        return null;
    }

    public synchronized static void shutdown(){
        AbstractServer.shutdownEventLoopGracefully(nioEventLoopGroup);
    }


}

