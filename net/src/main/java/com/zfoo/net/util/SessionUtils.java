package com.zfoo.net.util;

import com.zfoo.net.session.model.AttributeType;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.util.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import static com.zfoo.net.handler.BaseDispatcherHandler.SESSION_ATTRIBUTE_KEY;
/**
 * @author islandempty
 * @since 2021/7/15
 **/
public abstract class SessionUtils {
    private static final String CHANNEL_INFO_TEMPLATE = "[ip:{}][sid:{}][uid:{}]";

    public static boolean isActive(Session session){
        return session!=null&&session.getChannel().isActive();
    }

    public static boolean isActive(Channel channel){
        return channel!=null&&channel.isActive();
    }

    public static Session getSession(ChannelHandlerContext ctx){
        var sessionAttr = ctx.channel().attr(SESSION_ATTRIBUTE_KEY);
        return sessionAttr.get();
    }

    public static String sessionInfo(ChannelHandlerContext ctx) {
        var session = SessionUtils.getSession(ctx);
        if (session == null) {
            return StringUtils.format(CHANNEL_INFO_TEMPLATE, ctx.channel());
        }
        return sessionInfo(session);
    }

    public static String sessionInfo(Session session){
        if (session == null){
            return CHANNEL_INFO_TEMPLATE;
        }
        var remoteAddress = session.getAttribute(AttributeType.CHANNEL_REMOTE_ADDRESS);
        return StringUtils.format(CHANNEL_INFO_TEMPLATE, remoteAddress, session.getSid(), session.getAttribute(AttributeType.UID));
    }
}

