package com.zfoo.net.handler.codec.udp;

import com.zfoo.net.NetContext;
import com.zfoo.net.packet.model.DecodedPacketInfo;
import com.zfoo.net.packet.model.EncodePacketInfo;
import com.zfoo.net.packet.model.UdpPacketAttachment;
import com.zfoo.net.packet.service.PacketService;
import com.zfoo.net.util.SessionUtils;

import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.protocol.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;


/**
 * @author islandempty
 * @since 2021/7/23
 **/
public class UdpCodecHandler extends MessageToMessageCodec<DatagramPacket, EncodePacketInfo> {

    private static final Logger logger = LoggerFactory.getLogger(UdpCodecHandler.class);


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        ByteBuf in = datagramPacket.content();

        // 不够读一个int
        if (in.readableBytes() <= PacketService.PACKET_HEAD_LENGTH) {
            return;
        }

        in.markReaderIndex();
        var length = in.readInt();

        // 如果长度非法，则抛出异常断开连接
        if (length < 0) {
            throw new IllegalArgumentException(StringUtils.format("[session:{}]的包头长度[length:{}]非法"
                    , SessionUtils.sessionInfo(channelHandlerContext), length));
        }

        // ByteBuf里的数据太小
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        ByteBuf tmpByteBuf = null;
        try {
            tmpByteBuf = in.readRetainedSlice(length);
            DecodedPacketInfo packetInfo = NetContext.getPacketService().read(tmpByteBuf);
            var sender = datagramPacket.sender();
            packetInfo.setPacketAttachment(UdpPacketAttachment.valueOf(sender.getHostString(), sender.getPort()));
            list.add(packetInfo);
        } catch (Exception e) {
            logger.error("exception异常", e);
            throw e;
        } catch (Throwable t) {
            logger.error("throwable错误", t);
            throw t;
        } finally {
            ReferenceCountUtil.release(tmpByteBuf);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, EncodePacketInfo out, List<Object> list) throws Exception {
        try {
            var byteBuf = channelHandlerContext.alloc().ioBuffer();
            var udpPacketAttachment = (UdpPacketAttachment) out.getPacketAttachment();

            NetContext.getPacketService().write(byteBuf, out.getPacket(), out.getPacketAttachment());
            list.add(new DatagramPacket(byteBuf, new InetSocketAddress(udpPacketAttachment.getHost(), udpPacketAttachment.getPort())));
        } catch (Exception e) {
            logger.error("[{}]编码exception异常", JsonUtils.object2String(out), e);
            throw e;
        } catch (Throwable t) {
            logger.error("[{}]编码throwable错误", JsonUtils.object2String(out), t);
            throw t;
        }
    }


}

