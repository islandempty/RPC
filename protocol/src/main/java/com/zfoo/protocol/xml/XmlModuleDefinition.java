/*
 * Copyright (C) 2020 The zfoo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.zfoo.protocol.xml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

/**
 * @author islandempty
 */
@JsonPropertyOrder({"name", "minId", "maxId", "version"})
public class XmlModuleDefinition {

    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private byte id;

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

    public byte getId() {
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
