package com.zfoo.net.handler.idle;

import com.zfoo.net.packet.common.Heartbeat;
import com.zfoo.net.packet.model.EncodedPacketInfo;
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
public class ClientIdleHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientIdleHandler.class);

    private static final EncodedPacketInfo heartbeatPacket = EncodedPacketInfo.valueOf(Heartbeat.getInstance(), null);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                logger.warn("client sends heartbeat packet to {}", SessionUtils.sessionInfo(ctx));
                ctx.channel().writeAndFlush(heartbeatPacket);
            }
        }

    }
}

