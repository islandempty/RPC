package com.zfoo.scheduler.schema;

import com.zfoo.protocol.util.StringUtils;
import com.zfoo.scheduler.SchedulerContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author islandempty
 * @since 2021/7/2
 **/
public class SchedulerDefinitionParser implements BeanDefinitionParser {

    private final String SCHEDULER_ID = "id";

    @Override
    public AbstractBeanDefinition parse(Element element, ParserContext parserContext) {
        Class<?> clazz;
        String name;
        BeanDefinitionBuilder builder;

        //注册SchedulerSpringContext
        clazz = SchedulerContext.class;
        name = StringUtils.uncapitalize(clazz.getName());
        builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        parserContext.getRegistry().registerBeanDefinition(name,builder.getBeanDefinition());

        //注册SchedulerRegisterProcessor
        clazz = SchedulerRegisterProcessor.class;
        name = StringUtils.uncapitalize(clazz.getName());
        builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        parserContext.getRegistry().registerBeanDefinition(name, builder.getBeanDefinition());

        return builder.getBeanDefinition();
    }
}

