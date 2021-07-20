package com.zfoo.net.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author islandempty
 * @since 2021/7/18
 **/
public class NamespaceHandler extends NamespaceHandlerSupport {

    private final String NET_TAG = "config";

    @Override
    public void init() {
        registerBeanDefinitionParser(NET_TAG,new NetDe);
    }
}

