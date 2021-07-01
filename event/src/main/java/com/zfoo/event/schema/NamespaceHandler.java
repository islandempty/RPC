package com.zfoo.event.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 *
 * Spring的自定义标签解析
 *
 * @author islandempty
 * @since 2021/6/29
 **/

public class NamespaceHandler extends NamespaceHandlerSupport {

    public static final String EVENT = "event";

    @Override
    public void init() {
        registerBeanDefinitionParser(EVENT,new EventDefinitionParser());
    }
}

