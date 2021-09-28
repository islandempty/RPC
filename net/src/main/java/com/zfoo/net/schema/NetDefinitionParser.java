package com.zfoo.net.schema;

import com.ie.util.DomUtils;
import com.zfoo.net.NetContext;
import com.zfoo.net.config.manager.ConfigManager;
import com.zfoo.net.config.model.*;
import com.zfoo.net.consumer.service.Consumer;
import com.zfoo.net.dispatcher.manager.PacketDispatcher;
import com.zfoo.net.packet.service.PacketService;
import com.zfoo.net.session.manager.SessionManager;
import com.zfoo.protocol.ProtocolManager;
import com.zfoo.protocol.registration.ProtocolModule;
import com.zfoo.protocol.util.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;



/**
 * @author islandempty
 * @since 2021/7/25
 **/
public class NetDefinitionParser implements BeanDefinitionParser {

    @Override
    public AbstractBeanDefinition parse(Element element, ParserContext parserContext) {
        Class<?> clazz;
        String name;
        BeanDefinitionBuilder builder;

        // 注册NetConfig
        parseNetConfig(element, parserContext);

        // 注册NetSpringContext
        clazz = NetContext.class;
        name = StringUtils.uncapitalize(clazz.getName());
        builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        parserContext.getRegistry().registerBeanDefinition(name, builder.getBeanDefinition());

        // 注册ConfigManager
        clazz = ConfigManager.class;
        builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        builder.addPropertyReference("localConfig", NetConfig.class.getCanonicalName());
        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(), builder.getBeanDefinition());

        // 注册NetProcessor
        clazz = NetProcessor.class;
        builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(), builder.getBeanDefinition());

        // 注册ProtocolManager
        clazz = ProtocolManager.class;
        builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(), builder.getBeanDefinition());

        // 注册PacketService
        clazz = PacketService.class;
        builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(), builder.getBeanDefinition());

        // 注册PacketDispatcherManager
        clazz = PacketDispatcher.class;
        builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(), builder.getBeanDefinition());

        // 注册Consumer
        clazz = Consumer.class;
        builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(), builder.getBeanDefinition());

        // 注册SessionManager
        clazz = SessionManager.class;
        builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(), builder.getBeanDefinition());

        return builder.getBeanDefinition();
    }


    private void parseNetConfig(Element element, ParserContext parserContext) {
        var clazz = NetConfig.class;
        var builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);

        resolvePlaceholder("id", "id", builder, element, parserContext);
        resolvePlaceholder("protocol-location", "protocolLocation", builder, element, parserContext);
        resolvePlaceholder("generate-js-protocol", "generateJsProtocol", builder, element, parserContext);
        resolvePlaceholder("generate-cs-protocol", "generateCsProtocol", builder, element, parserContext);
        resolvePlaceholder("generate-lua-protocol", "generateLuaProtocol", builder, element, parserContext);
        resolvePlaceholder("fold-protocol", "foldProtocol", builder, element, parserContext);
        resolvePlaceholder("protocol-param", "protocolParam", builder, element, parserContext);

        var registryElement = DomUtils.getFirstChildElementByTagName(element, "registry");
        if (registryElement != null) {
            parseRegistryConfig(registryElement, parserContext);
            builder.addPropertyReference("registryConfig", RegistryConfig.class.getCanonicalName());
        }

        var monitorElement = DomUtils.getFirstChildElementByTagName(element, "monitor");
        if (monitorElement != null) {
            parseMonitorConfig(monitorElement, parserContext);
            builder.addPropertyReference("monitorConfig", MonitorConfig.class.getCanonicalName());
        }

        var hostElement = DomUtils.getFirstChildElementByTagName(element, "host");
        if (hostElement != null) {
            builder.addPropertyReference("hostConfig", HostConfig.class.getCanonicalName());
            parseHostConfig(hostElement, parserContext);
        }

        var providerElement = DomUtils.getFirstChildElementByTagName(element, "provider");
        if (providerElement != null) {
            builder.addPropertyReference("providerConfig", ProviderConfig.class.getCanonicalName());
            parseProviderConfig(providerElement, parserContext);
        }

        var consumerElement = DomUtils.getFirstChildElementByTagName(element, "consumer");
        if (consumerElement != null) {
            parseConsumerConfig(consumerElement, parserContext);
            builder.addPropertyReference("consumerConfig", ConsumerConfig.class.getCanonicalName());
        }

        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(), builder.getBeanDefinition());
    }



    private void parseRegistryConfig(Element element, ParserContext parserContext) {
        var clazz = RegistryConfig.class;
        var builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);

        resolvePlaceholder("center", "center", builder, element, parserContext);
        resolvePlaceholder("user", "user", builder, element, parserContext);
        resolvePlaceholder("password", "password", builder, element, parserContext);
        var addressMap = parseAddress(element, parserContext);
        builder.addPropertyValue("addressMap", addressMap);
        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(), builder.getBeanDefinition());
    }

    private void parseMonitorConfig(Element element, ParserContext parserContext) {
        var clazz = MonitorConfig.class;
        var builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);

        resolvePlaceholder("center", "center", builder, element, parserContext);
        resolvePlaceholder("user", "user", builder, element, parserContext);
        resolvePlaceholder("password", "password", builder, element, parserContext);
        var addressMap = parseAddress(element, parserContext);
        builder.addPropertyValue("addressMap", addressMap);
        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(), builder.getBeanDefinition());
    }

    private void parseHostConfig(Element element, ParserContext parserContext) {
        var clazz = HostConfig.class;
        var builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);

        resolvePlaceholder("center", "center", builder, element, parserContext);
        resolvePlaceholder("user", "user", builder, element, parserContext);
        resolvePlaceholder("password", "password", builder, element, parserContext);
        var addressMap = parseAddress(element, parserContext);
        builder.addPropertyValue("addressMap", addressMap);
        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(), builder.getBeanDefinition());
    }

    private void parseProviderConfig(Element element, ParserContext parserContext) {
        var clazz = ProviderConfig.class;
        var builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);

        resolvePlaceholder("dispatch", "dispatch", builder, element, parserContext);
        resolvePlaceholder("dispatch-thread", "dispatchThread", builder, element, parserContext);
        resolvePlaceholder("address", "address", builder, element, parserContext);

        var providerModules = parseModules("provider", element, parserContext);
        builder.addPropertyValue("modules", providerModules);
        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(), builder.getBeanDefinition());
    }

    private void parseConsumerConfig(Element element,ParserContext parserContext){
        var clazz = ConsumerConfig.class;
        var builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);

        resolvePlaceholder("load-balancer","loadBalancer",builder,element,parserContext);

        var consumerModules = parseModules("consumer", element, parserContext);
        builder.addPropertyValue("modules",consumerModules);
        parserContext.getRegistry().registerBeanDefinition(clazz.getCanonicalName(),builder.getBeanDefinition());
    }

    private ManagedList<BeanDefinitionHolder> parseModules(String param, Element element, ParserContext parserContext) {
        var moduleElementList = DomUtils.getChildElementsByTagName(element, "module");
        var modules = new ManagedList<BeanDefinitionHolder>();
        var environment = parserContext.getReaderContext().getEnvironment();
        for (var i = 0; i < moduleElementList.size(); i++) {
            var addressElement = moduleElementList.get(i);
            var clazz = ProtocolModule.class;
            var builder = BeanDefinitionBuilder.rootBeanDefinition(clazz);

            builder.addConstructorArgValue(environment.resolvePlaceholders(addressElement.getAttribute("name")));

            modules.add(new BeanDefinitionHolder(builder.getBeanDefinition(), StringUtils.format("{}.{}{}", clazz.getCanonicalName(), param, i)));
        }
        return modules;
    }

    private ManagedMap<String, String> parseAddress(Element element, ParserContext parserContext){
        var addressElementList = DomUtils.getChildElementsByTagName(element, "address");
        var addressMap = new ManagedMap<String,String>();

        for (var addressElement : addressElementList) {
            var name = addressElement.getAttribute("name");
            var urlAttribute = addressElement.getAttribute("url");
            var url = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(urlAttribute);
            addressMap.put(name, url);
        }
        return addressMap;
    }

    private void resolvePlaceholder(String attributeName, String fieldName, BeanDefinitionBuilder builder, Element element, ParserContext parserContext){
        var attribute = element.getAttribute(attributeName);
        var environment = parserContext.getReaderContext().getEnvironment();
        var placeholders = environment.resolvePlaceholders(attribute);
        builder.addPropertyValue(fieldName,placeholders);
    }
}

