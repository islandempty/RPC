package com.zfoo.net.handler.codec.tcp;

import com.zfoo.net.NetContext;
import com.zfoo.net.packet.model.DecodedPacketInfo;
import com.zfoo.net.packet.model.EncodePacketInfo;
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
public class TcpCodeHandler extends ByteToMessageCodec<EncodePacketInfo> {

    private static final Logger logger = LoggerFactory.getLogger(TcpCodeHandler.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //不够读一个int
        if (in.readableBytes() <= PacketService.PACKET_HEAD_LENGTH){
            return;
        }
        in.markReaderIndex();
        var length = in.readInt();

        //如果长度非法,则抛出异常断开连接
        if (length < 0){
            throw new IllegalArgumentException(StringUtils.format("[session:{}]的包头长度[length:{}]非法"
                    , SessionUtils.sessionInfo(ctx), length));
        }
        //markReaderIndex和resetReaderIndex是一个成对的操作。markReaderIndex可以打一个标记，调用resetReaderIndex可以把readerIndex重置到原来打标记的位置。

        //ByteBuf里的数据大小
        if (in.readableBytes() < length){
            in.resetReaderIndex();
        }

        ByteBuf tmpByteBuf = null;
        try {
            tmpByteBuf = in.readRetainedSlice(length);
            DecodedPacketInfo packetInfo = NetContext.getPacketService().read(tmpByteBuf);
            out.add(packetInfo);
        }catch (Exception e) {
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
    protected void encode(ChannelHandlerContext ctx, EncodePacketInfo PacketInfo, ByteBuf out) throws Exception {
        try {
            NetContext.getPacketService().write(out, PacketInfo.getPacket(), PacketInfo.getPacketAttachment());
        }catch (Exception e) {
            logger.error("[session:{}][{}]编码exception异常", SessionUtils.sessionInfo(ctx), PacketInfo.getPacket().getClass().getSimpleName(), e);
            throw e;
        } catch (Throwable t) {
            logger.error("[session:{}][{}]编码throwable错误", SessionUtils.sessionInfo(ctx), PacketInfo.getPacket().getClass().getSimpleName(), t);
            throw t;
        }
    }
}

