package com.zfoo.net.handler;

import com.zfoo.net.NetContext;
import com.zfoo.net.packet.model.DecodedPacketInfo;
import com.zfoo.net.session.model.AttributeType;
import com.zfoo.net.session.model.Session;
import com.zfoo.net.util.SessionUtils;
import com.zfoo.protocol.util.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author islandempty
 * @since 2021/7/15
 **/

//标注一个channel handler可以被多个channel安全地共享。
@ChannelHandler.Sharable
public class BaseDispatcherHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(BaseDispatcherHandler.class);

    //定义一个attribute
    public static final AttributeKey<Session> SESSION_ATTRIBUTE_KEY =AttributeKey.valueOf("session");

    public static Session initChannel(Channel channel){
        //获取value
        var sessionAttr = channel.attr(SESSION_ATTRIBUTE_KEY);
        var session = new Session(channel);
        //value设置为session
        var setSuccessful = sessionAttr.compareAndSet(null, session);
        if (!setSuccessful){
            channel.close();
            throw new RuntimeException(StringUtils.format("无法设置[channel:{}]的session", channel));
        }
        try{
            session.putAttribute(AttributeType.CHANNEL_REMOTE_ADDRESS, StringUtils.substringAfterFirst(channel.remoteAddress().toString(), StringUtils.SLASH));
        }catch (Throwable e){
            // do nothing
            // to avoid: io.netty.channel.unix.Errors$NativeIoException: readAddress(..) failed: Connection reset by peer
            // 有些情况当建立连接过后迅速关闭，这个时候取remoteAddress会有异常
        }

        return session;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        var session = SessionUtils.getSession(ctx);
        if (session == null){
            return;
        }
        DecodedPacketInfo decodedPacketInfo = (DecodedPacketInfo) msg;
        NetContext.getDispatcher().receive(session, decodedPacketInfo.getPacket(),decodedPacketInfo.getPacketAttachment());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  {
        try {
            logger.error("[session{}]未知异常", SessionUtils.sessionInfo(ctx), cause);
        } finally {
            ctx.close();
        }
    }
}

