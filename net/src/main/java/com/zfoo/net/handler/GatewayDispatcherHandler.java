package com.zfoo.net.handler;

import com.zfoo.event.manager.EventBus;
import com.zfoo.net.NetContext;
import com.zfoo.net.consumer.balancer.ConsistentHashConsumerLoadBalancer;
import com.zfoo.net.core.gateway.IGatewayLoadBalancer;
import com.zfoo.net.core.gateway.model.GatewaySessionInactiveEvent;
import com.zfoo.net.packet.common.Heartbeat;
import com.zfoo.net.packet.common.Ping;
import com.zfoo.net.packet.common.Pong;
import com.zfoo.net.packet.model.DecodedPacketInfo;
import com.zfoo.net.packet.model.GatewayPacketAttachment;
import com.zfoo.net.packet.model.IPacketAttachment;
import com.zfoo.net.packet.model.SignalPacketAttachment;
import com.zfoo.net.session.model.AttributeType;
import com.zfoo.net.session.model.Session;
import com.zfoo.net.util.SessionUtils;
import com.zfoo.protocol.IPacket;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.protocol.util.StringUtils;
import com.zfoo.scheduler.util.TimeUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;

/**
 * @author islandempty
 * @since 2021/7/20
 **/
@ChannelHandler.Sharable
public class GatewayDispatcherHandler extends BaseDispatcherHandler{

    private static final Logger logger = LoggerFactory.getLogger(GatewayDispatcherHandler.class);

    private BiFunction<Session, IPacket , Boolean> packetFilter;

    public GatewayDispatcherHandler(BiFunction<Session, IPacket, Boolean> packetFilter) {
        this.packetFilter = packetFilter;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //请求者的session，一般是serverSession
        var session = SessionUtils.getSession(ctx);
        if (session == null){
            return;
        }

        var decodedPacketInfo = (DecodedPacketInfo)msg;
        var packet = decodedPacketInfo.getPacket();
        if (packet.protocolId() == Heartbeat.heartbeatProtocolId()){
            return;
        }
        if (packet.protocolId() == Ping.pingProtocolId()){
            NetContext.getDispatcher().send(session , Pong.valueOf(TimeUtils.now()),null);
            return;
        }

        //过滤非法包
        if (packetFilter !=null && packetFilter.apply(session , packet)){
            throw new IllegalArgumentException(StringUtils.format("[session:{}]发送了一个非法包[{}]"
                    , SessionUtils.sessionInfo(ctx), JsonUtils.object2String(packet)));
        }

        var signalAttachment = (SignalPacketAttachment)decodedPacketInfo.getPacketAttachment();
        var gatewayPacketAttachment = new GatewayPacketAttachment(session, signalAttachment);

        //网关优先使用IGatewayLoadBalancer作为一致性hash的计算参数，然后才会使用客户端的session做参数
        if (packet instanceof IGatewayLoadBalancer){
            var loadBalancerConsistentHashObject = ((IGatewayLoadBalancer) packet).loadBalancerConsistentHashObject();
            gatewayPacketAttachment.useExecutorConsistentHash(loadBalancerConsistentHashObject);
            forwardingPacket(packet,gatewayPacketAttachment,loadBalancerConsistentHashObject);
            return;
        }else {
            //使用用户的uid做一致性hash
            var uid = (Long) session.getAttribute(AttributeType.UID);
            if (uid != null){
                forwardingPacket(packet, gatewayPacketAttachment ,uid);
                return;
            }
        }
        // 再使用session的sid做一致性hash，因为每次客户端连接过来sid都会改变，所以客户端重写建立连接的话可能会被路由到其它的服务器
        // 如果有特殊需求的话，可以考虑去重写网关的转发策略
        var sid = session.getSid();
        forwardingPacket(packet,gatewayPacketAttachment,sid);
    }

    public void forwardingPacket(IPacket packet, IPacketAttachment attachment, Object argument){
        try {
            var session = ConsistentHashConsumerLoadBalancer.getInstance().loadBalancer(packet, argument);
            NetContext.getDispatcher().send(session,packet,attachment);
        }catch (Exception e){
            logger.error("网关发生异常", e);
        }catch (Throwable t){
            logger.error("网关发生错误", t);
        }

    }



    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        var session = SessionUtils.getSession(ctx);
        if (session == null){
            return;
        }

        var sid = session.getSid();
        var uid = (Long)session.getAttribute(AttributeType.UID);
        EventBus.asyncSubmit(GatewaySessionInactiveEvent.valueOf(sid, uid == null ? 0 : uid.longValue()));

        super.channelInactive(ctx);
    }


}

