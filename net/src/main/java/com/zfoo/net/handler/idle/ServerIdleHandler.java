package com.zfoo.net.handler.idle;

import com.zfoo.net.util.SessionUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author islandempty
 * @since 2021/7/23
 **/
@ChannelHandler.Sharable
public class ServerIdleHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerIdleHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                logger.warn("[channel:{}] is time out for close", SessionUtils.sessionInfo(ctx));
            }
            ctx.close();
        }
    }
}

