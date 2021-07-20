package com.zfoo.net.handler;

import com.zfoo.net.NetContext;
import com.zfoo.net.session.model.AttributeType;
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
public class ClientDispatcherHandler extends BaseDispatcherHandler{

    private static final Logger logger = LoggerFactory.getLogger(ClientDispatcherHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info("client channel [{}] is active", SessionUtils.sessionInfo(ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        var session = SessionUtils.getSession(ctx);
        if (session!=null){
            var consumeAttribute = session.getAttribute(AttributeType.CONSUMER);
            NetContext.getSessionManager().removeServerSession(session);

            //如果是消费者inactive，还要触发客户端消费者检查事件,以便重新连接
            if (consumeAttribute !=null){
                NetContext.getConfigManager().getRegistry().checkConsumer();
            }
        }
        logger.warn("[channel:{}] is inactive", SessionUtils.sessionInfo(ctx));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("[session{}]未知异常", SessionUtils.sessionInfo(ctx), cause);
    }
}

