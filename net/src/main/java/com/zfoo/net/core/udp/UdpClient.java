package com.zfoo.net.core.udp;

import com.ie.util.net.HostAndPort;
import com.zfoo.net.NetContext;
import com.zfoo.net.core.AbstractClient;
import com.zfoo.net.handler.BaseDispatcherHandler;
import com.zfoo.net.handler.ClientDispatcherHandler;
import com.zfoo.net.handler.codec.udp.UdpCodecHandler;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.exception.ExceptionUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.checkerframework.checker.units.qual.C;

/**
 * @author islandempty
 * @since 2021/7/25
 **/
public class UdpClient extends AbstractClient {

    public UdpClient(HostAndPort host) {
        super(host);
    }

    @Override
    public synchronized Session start() {
        try {
            this.bootstrap = new Bootstrap();
            this.bootstrap.group(nioEventLoopGroup)
                    .channel(Epoll.isAvailable()? EpollDatagramChannel.class: NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new ChannelHandlerInitializer());

            // bind(0)随机选择一个端口
            var channelFuture = bootstrap.bind(0).sync();
            //等任务完成
            channelFuture.syncUninterruptibly();

            if (channelFuture.isSuccess()){
                    if (channelFuture.channel().isActive()){
                        var channel = channelFuture.channel();
                        var session = BaseDispatcherHandler.initChannel(channel);
                        NetContext.getSessionManager().addClientSession(session);
                        logger.info("UdpClient started at [{}]", channel.localAddress());
                        return session;
                    }
            }else if (channelFuture.cause() != null) {
                logger.error(ExceptionUtils.getMessage(channelFuture.cause()));
            } else {
                logger.error("启动客户端[client:{}]未知错误", this);
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getMessage(e));
        }
        return null;
    }

    @Override
    public ChannelInitializer<? extends Channel> channelChannelInitializer() {
        return new ChannelHandlerInitializer();
    }

    private static class ChannelHandlerInitializer extends ChannelInitializer<Channel>{

        @Override
        protected void initChannel(Channel channel) throws Exception {
            channel.pipeline().addLast(new UdpCodecHandler());
            channel.pipeline().addLast(new ClientDispatcherHandler());
        }
    }
}

