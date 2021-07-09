package com.zfoo.protocol.xml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

/**
 * @author islandempty
 * @since 2021/7/9
 **/
//序列化时生效，将返回的json按字段排序
@JsonPropertyOrder({"name","minId","maxId","version"})
public class XmlModuleDefinition {

    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private Byte id;

    @JacksonXmlProperty(isAttribute = true, localName = "name")
    private String name;

    @JacksonXmlProperty(isAttribute = true, localName = "minId")
    private short minId;

    @JacksonXmlProperty(isAttribute = true, localName = "maxId")
    private short maxId;

    @JacksonXmlProperty(isAttribute = true, localName = "version")
    private String version;

    @JacksonXmlProperty(localName = "protocol")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<XmlProtocolDefinition> protocols;

    public Byte getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public short getMinId() {
        return minId;
    }

    public short getMaxId() {
        return maxId;
    }

    public String getVersion() {
        return version;
    }

    public List<XmlProtocolDefinition> getProtocols() {
        return protocols;
    }
}

