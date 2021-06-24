package com.zfoo.storage.model.vo;

import org.springframework.core.io.Resource;

/**
 * @author islandempty
 * @since 2021/6/24
 **/
public class ResourceDef {

    private final Class<?> clazz;
    private final Resource resource;


    public ResourceDef(Class<?> clazz, Resource resource) {
        this.clazz = clazz;
        this.resource = resource;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Resource getResource() {
        return resource;
    }
}

