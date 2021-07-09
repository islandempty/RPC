package com.zfoo.protocol.xml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

/**
 * @author islandempty
 * @since 2021/7/9
 **/

@JsonPropertyOrder({"author","modules"})
@JacksonXmlRootElement(localName = "protocols")
public class XmlProtocols {

    @JacksonXmlProperty(isAttribute = true,localName = "author")
    private String author;

    @JacksonXmlProperty(localName = "modules")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<XmlModuleDefinition> modules;

    public String getAuthor() {
        return author;
    }

    public List<XmlModuleDefinition> getModules() {
        return modules;
    }
}


