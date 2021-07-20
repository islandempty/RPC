package com.zfoo.net.consumer.registry;

import com.ie.util.security.IdUtils;
import com.zfoo.net.config.model.ConsumerConfig;
import com.zfoo.net.config.model.ProviderConfig;
import com.zfoo.protocol.collection.CollectionUtils;
import com.zfoo.protocol.exception.ExceptionUtils;
import com.zfoo.protocol.registration.ProtocolModule;
import com.zfoo.protocol.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author islandempty
 * @since 2021/7/17
 **/
public class RegisterVO {

    private static final Logger logger = LoggerFactory.getLogger(RegisterVO.class);

    private static final String uuid = IdUtils.getUUID();

    private String id;
    private ProviderConfig providerConfig;
    private ConsumerConfig consumerConfig;

    public static boolean providerHasConsumerModule(RegisterVO provider, RegisterVO consumer){
        if (Objects.isNull(provider) || Objects.isNull(provider.providerConfig) || CollectionUtils.isEmpty(provider.providerConfig.getModules())
                || Objects.isNull(consumer) || Objects.isNull(consumer.consumerConfig) || CollectionUtils.isEmpty(consumer.consumerConfig.getModules())) {
            return false;
        }
        return provider.getProviderConfig().getModules().stream().anyMatch(it -> consumer.getConsumerConfig().getModules().contains(it));
    }

    public static RegisterVO valueOf(String id, ProviderConfig providerConfig, ConsumerConfig consumerConfig) {
        RegisterVO config = new RegisterVO();
        config.id = id;
        config.providerConfig = providerConfig;
        config.consumerConfig = consumerConfig;
        return config;
    }

    @Nullable
    public static RegisterVO parseString(String str) {
        try {
            var vo = new RegisterVO();
            var splits = str.split("\\|");

            vo.id = splits[0].trim();

            String providerAddress = null;

            for (int i = 1; i < splits.length; i++) {
                var s = splits[i].trim();
                if (s.startsWith("provider")) {
                    var providerModules = parseModules(s);
                    vo.providerConfig = ProviderConfig.valueOf(providerAddress, providerModules);
                } else if (s.startsWith("consumer")) {
                    var consumerModules = parseModules(s);
                    vo.consumerConfig = ConsumerConfig.valueOf(consumerModules);
                } else {
                    providerAddress = s;
                }
            }

            return vo;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getMessage(e));
            return null;
        }
    }

    private static List<ProtocolModule> parseModules(String str){
        var moduleSplits = StringUtils.substringBeforeLast(
                StringUtils.substringAfterFirst(str, StringUtils.LEFT_SQUARE_BRACKET),
                StringUtils.RIGHT_SQUARE_BRACKET
        ).split(StringUtils.COMMA);

        var modules = Arrays.stream(moduleSplits)
                .map(it -> it.trim())
                .map(it -> it.split(StringUtils.HYPHEN))
                .map(it -> new ProtocolModule(Byte.parseByte(it[0]), it[1], it[2]))
                .collect(Collectors.toList());
        return modules;
    }

    public String toProviderString() {
        return toString();
    }

    public String toConsumerString(){
        return toString() +
                StringUtils.SPACE + StringUtils.VERTICAL_BAR + StringUtils.SPACE +
                uuid;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append(id);

        if (Objects.nonNull(providerConfig)) {
            var providerAddress = providerConfig.getAddress();
            if (StringUtils.isBlank(providerAddress)) {
                throw new RuntimeException(StringUtils.format("providerConfig的address不能为空"));
            }
            builder.append(StringUtils.SPACE).append(StringUtils.VERTICAL_BAR).append(StringUtils.SPACE);
            builder.append(providerAddress);

            builder.append(StringUtils.SPACE).append(StringUtils.VERTICAL_BAR).append(StringUtils.SPACE);
            var providerModules = providerConfig.getModules().stream()
                    .map(it -> StringUtils.joinWith(StringUtils.HYPHEN, it.getId(), it.getName(), ProtocolModule.versionNumToStr(it.getVersion())))
                    .collect(Collectors.toList());
            builder.append(StringUtils.format("provider:[{}]"
                    , StringUtils.joinWith(StringUtils.COMMA + StringUtils.SPACE, providerModules.toArray())));
        }

        if (Objects.nonNull(consumerConfig)) {
            builder.append(StringUtils.SPACE).append(StringUtils.VERTICAL_BAR).append(StringUtils.SPACE);

            var consumerModules = consumerConfig.getModules().stream()
                    .map(it -> StringUtils.joinWith(StringUtils.HYPHEN, it.getId(), it.getName(), ProtocolModule.versionNumToStr(it.getVersion())))
                    .collect(Collectors.toList());
            builder.append(StringUtils.format("consumer:[{}]"
                    , StringUtils.joinWith(StringUtils.COMMA + StringUtils.SPACE, consumerModules.toArray())));
        }

        return builder.toString();
    }


    public String getId() {
        return id;
    }

    public ProviderConfig getProviderConfig() {
        return providerConfig;
    }

    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegisterVO that = (RegisterVO) o;
        return Objects.equals(id, that.id) && Objects.equals(providerConfig, that.providerConfig)
                && Objects.equals(consumerConfig, that.consumerConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, providerConfig, consumerConfig);
    }
}

