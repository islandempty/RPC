package com.zfoo.scheduler.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author islandempty
 * @since 2021/7/2
 **/
public class NamespaceHandler extends NamespaceHandlerSupport {

    public static final String SCHEDULER = "scheduler";

    @Override
    public void init() {
            registerBeanDefinitionParser(SCHEDULER ,new SchedulerDefinitionParser());
    }
}

