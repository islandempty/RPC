package com.zfoo.protocol.xml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author islandempty
 * @since 2021/7/9
 **/

@JsonPropertyOrder({"id", "location", "enhance"})
public class XmlProtocolDefinition {
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private short id;

    @JacksonXmlProperty(isAttribute = true, localName = "location")
    private String location;

    @JacksonXmlProperty(isAttribute = true, localName = "enhance")
    private boolean enhance = true;


    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isEnhance() {
        return enhance;
    }

    public void setEnhance(boolean enhance) {
        this.enhance = enhance;
    }
}

