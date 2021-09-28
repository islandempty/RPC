package com.zfoo.net.handler.codec.tcp;

import com.zfoo.net.NetContext;
import com.zfoo.net.packet.model.DecodedPacketInfo;
import com.zfoo.net.packet.model.EncodedPacketInfo;
import com.zfoo.net.packet.service.PacketService;
import com.zfoo.net.util.SessionUtils;
import com.zfoo.protocol.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * header(4byte) + protocolId(2byte) + packet
 * header = body(bytes.length) + protocolId.length(2byte)
 *
 * @author islandempty
 * @since 2021/7/23
 **/
public class TcpCodeHandler extends ByteToMessageCodec<EncodedPacketInfo>  {

    private static final Logger logger = LoggerFactory.getLogger(TcpCodeHandler.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 不够读一个int
        if (in.readableBytes() <= PacketService.PACKET_HEAD_LENGTH) {
            return;
        }
        in.markReaderIndex();
        var length = in.readInt();

        // 如果长度非法，则抛出异常断开连接
        if (length < 0) {
            throw new IllegalArgumentException(StringUtils.format("[session:{}]的包头长度[length:{}]非法"
                    , SessionUtils.sessionInfo(ctx), length));
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
            out.add(packetInfo);
        } catch (Exception e) {
            logger.error("[session:{}]解码exception异常", SessionUtils.sessionInfo(ctx), e);
            throw e;
        } catch (Throwable t) {
            logger.error("[session:{}]解码throwable错误", SessionUtils.sessionInfo(ctx), t);
            throw t;
        } finally {
            ReferenceCountUtil.release(tmpByteBuf);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, EncodedPacketInfo packetInfo, ByteBuf out) {
        try {
            NetContext.getPacketService().write(out, packetInfo.getPacket(), packetInfo.getPacketAttachment());
        } catch (Exception e) {
            logger.error("[session:{}][{}]编码exception异常", SessionUtils.sessionInfo(ctx), packetInfo.getPacket().getClass().getSimpleName(), e);
            throw e;
        } catch (Throwable t) {
            logger.error("[session:{}][{}]编码throwable错误", SessionUtils.sessionInfo(ctx), packetInfo.getPacket().getClass().getSimpleName(), t);
            throw t;
        }
    }
}

