package com.zfoo.storage.model.config;

/**
 * @author islandempty
 * @since 2021/6/23
 **/
public class StorageConfig {

    private String id;

    private String scanPackage;

    private String resourceLocation;

    private String resourceSuffix;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public String getResourceSuffix() {
        return resourceSuffix;
    }

    public void setResourceSuffix(String resourceSuffix) {
        this.resourceSuffix = resourceSuffix;
    }
}

