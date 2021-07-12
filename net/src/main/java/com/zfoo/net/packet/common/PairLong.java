package com.zfoo.net.packet.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zfoo.protocol.IPacket;

import java.util.Comparator;

/**
 * @author islandempty
 * @since 2021/7/12
 **/
public class PairLong implements IPacket {

    public static final transient short PROTOCOL_ID = 111;

    public static transient final Comparator<PairLong> NATURAL_VALUE_COMPARATOR = (a,b) -> Long.compare(a.getValue(), b.getValue());


    /**
     * java中long数据能表示的范围比js中number大,在跟前端交互时，这样也就意味着部分数值在js中存不下(变成不准确的值)。
     * 解决办法可以这样：
     * 使用ToStringSerializer注解，让系统序列化时，保留相关精度。
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private long key;

    @JsonSerialize(using = ToStringSerializer.class)
    private long value;

    public static PairLong valueOf(long key, long value) {
        var pair = new PairLong();
        pair.key = key;
        pair.value = value;
        return pair;
    }

    /**
     * 这个类的协议号
     *
     * @return 协议号
     */
    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}

