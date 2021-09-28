package com.zfoo.net.handler;

import com.zfoo.event.manager.EventBus;
import com.zfoo.net.NetContext;
import com.zfoo.net.core.tcp.model.ServerSessionInactiveEvent;
import com.zfoo.net.session.model.Session;
import com.zfoo.net.util.SessionUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author islandempty
 * @since 2021/7/20
 **/
@ChannelHandler.Sharable
public class ServerDispatcherHandler extends BaseDispatcherHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerDispatcherHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        var session = initChannel(ctx.channel());
        NetContext.getSessionManager().addServerSession(session);
        logger.info("server channel [{}] is active", SessionUtils.sessionInfo(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        var session = SessionUtils.getSession(ctx);
        if (session == null) {
            return;
        }
        NetContext.getSessionManager().removeServerSession(session);
        EventBus.asyncSubmit(ServerSessionInactiveEvent.valueOf(session));
        logger.warn("[channel:{}] is inactive", SessionUtils.sessionInfo(ctx));
    }
}

