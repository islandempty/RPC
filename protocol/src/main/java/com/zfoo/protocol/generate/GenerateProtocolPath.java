package com.zfoo.protocol.generate;

import com.zfoo.protocol.registration.IProtocolRegistration;
import com.zfoo.protocol.util.ReflectionUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.zfoo.protocol.util.StringUtils.TAB;
/**
 * @author islandempty
 * @since 2021/7/8
 **/
public abstract class GenerateProtocolPath {
    /**
     * 生成协议的过滤器，默认不过滤
     */
    public static Predicate<IProtocolRegistration> generateProtocolFilter = registration -> true;

    public static AtomicInteger index = new AtomicInteger();

    public static StringBuilder addTab(StringBuilder builder , int deep){
        //重复 count 次的串联
        builder.append(TAB.repeat((Math.max(0,deep))));
        return builder;
    }
    public static void clear(){
        generateProtocolFilter = null;
        index = null;
    }
    public static void generate(IProtocolRegistration[] protocols, GenerateOperation generateOperation)throws IOException {
        //如果没有生成的协议直接返回
        var generateProtocolFlag = Arrays.stream(generateOperation.getClass().getDeclaredFields())
                .filter(it -> it.getName().startsWith("generate"))
                .peek(it -> ReflectionUtils.makeAccessible(it))
                .map(it -> ReflectionUtils.getField(it, generateOperation))
                .filter(it -> it instanceof Boolean)
                .anyMatch(it -> ((Boolean) it).booleanValue() == true);

        if (!generateProtocolFlag){
            return;
        }
        // 外层需要生成的协议
        var outsideGenerateProtocols = Arrays.stream(protocols)
                .filter(it -> Objects.nonNull(it))
                .filter(it -> generateProtocolFilter.test(it))
                .collect(Collectors.toList());


    }
}

