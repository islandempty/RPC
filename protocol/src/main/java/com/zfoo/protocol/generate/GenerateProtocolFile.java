package com.zfoo.protocol.generate;

import com.zfoo.protocol.ProtocolManager;
import com.zfoo.protocol.registration.IProtocolRegistration;
import com.zfoo.protocol.registration.ProtocolAnalysis;
import com.zfoo.protocol.registration.ProtocolRegistration;
import com.zfoo.protocol.util.ReflectionUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.zfoo.protocol.util.StringUtils.TAB;

/**
 * @author islandempty
 * @since 2021/7/8
 **/
public class GenerateProtocolFile {
    /**
     * 生成协议的过滤器，默认不过滤
     */
    public static Predicate<IProtocolRegistration> generateProtocolFilter = registration -> true;

    public static AtomicInteger index = new AtomicInteger();

    public static StringBuilder addTab(StringBuilder builder, int deep) {
        builder.append(TAB.repeat(Math.max(0, deep)));
        return builder;
    }

    public static void clear() {
        generateProtocolFilter = null;
        index = null;
    }

    public static void generate(GenerateOperation generateOperation) throws IOException {
        var protocols = ProtocolManager.protocols;

        // 如果没有需要生成的协议则直接返回
        var generateProtocolFlag = Arrays.stream(generateOperation.getClass().getDeclaredFields())
                .filter(it -> it.getName().startsWith("generate"))
                .peek(it -> ReflectionUtils.makeAccessible(it))
                .map(it -> ReflectionUtils.getField(it, generateOperation))
                .filter(it -> it instanceof Boolean)
                .anyMatch(it -> ((Boolean) it).booleanValue() == true);

        if (!generateProtocolFlag) {
            return;
        }

        // 外层需要生成的协议
        var outsideGenerateProtocols = Arrays.stream(protocols)
                .filter(it -> Objects.nonNull(it))
                .filter(it -> generateProtocolFilter.test(it))
                .collect(Collectors.toList());

        // 需要生成的子协议，因为外层协议的内部有其它协议
        var insideGenerateProtocols = outsideGenerateProtocols.stream()
                .map(it -> ProtocolAnalysis.getAllSubProtocolIds(it.protocolId()))
                .flatMap(it -> it.stream())
                .map(it -> protocols[it])
                .distinct()
                .collect(Collectors.toList());

        var allGenerateProtocols = new HashSet<IProtocolRegistration>();
        allGenerateProtocols.addAll(outsideGenerateProtocols);
        allGenerateProtocols.addAll(insideGenerateProtocols);

        // 通过协议号，从小到大排序
        var allSortedGenerateProtocols = allGenerateProtocols.stream()
                .sorted((a, b) -> a.protocolId() - b.protocolId())
                .collect(Collectors.toList());

        // 解析协议的文档注释
        GenerateProtocolDocument.initProtocolDocument(allSortedGenerateProtocols);


        // 计算协议生成的路径
        if (generateOperation.isFoldProtocol()) {
            GenerateProtocolPath.initProtocolPath(allSortedGenerateProtocols);
        }


        // 预留参数，以后可能会用，比如给Lua修改一个后缀名称
        var protocolParam = generateOperation.getProtocolParam();
    }

}

