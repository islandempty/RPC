package com.zfoo.protocol.generate;

import com.zfoo.protocol.model.Pair;
import com.zfoo.protocol.util.AssertionUtils;
import com.zfoo.protocol.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 生成协议的时候，协议的文档注释和字段注释会使用这个类
 *
 * @author islandempty
 * @since 2021/7/8
 **/
public abstract class GenerateProtocolDocument {

    // 临时变量，启动完成就会销毁，协议的文档，外层map的key为协议类；pair的key为总的注释，value为属性字段的注释，value表示的map的key为属性名称
    // 比如在Test中的ComplexObject生成的pari是如下格式
    /**
     * key docTitle:
     * // 复杂的对象
     * // 包括了各种复杂的结构，数组，List，Set，Map
     * //
     * // @author jaysunxiao
     * // @version 1.0
     * <p>
     * value aa:
     * // byte的包装类型
     * // 优先使用基础类型，包装类型会有装箱拆箱
     */
    private static Map<Short, Pair<String, Map<String, String>>> tempProtocolDocumentMap = new HashMap<>();

    public static void clear() {
        tempProtocolDocumentMap.clear();
        tempProtocolDocumentMap = null;
    }
    /**
     * 此方法仅在生成协议的时候调用，一旦运行，不能调用
     */
    public static Pair<String, Map<String, String>> getProtocolDocument(short protocolId) {
        AssertionUtils.notNull(tempProtocolDocumentMap, "[{}]已经初始完成，初始化完成过后不能调用getProtocolDocument", GenerateProtocolDocument.class.getSimpleName());

        var protocolDocument = tempProtocolDocumentMap.get(protocolId);
        if (protocolDocument == null) {
            return new Pair<>(StringUtils.EMPTY, Collections.emptyMap());
        }
        return protocolDocument;
    }



}

