package com.ie.hotdeploy.model;

/**
 * @author islandempty
 * @since 2021/6/1
 **/

//类文件定义
public class ClassFileDef {

    private String path;
    private String className;
    private byte[] data;
    private long lastModifyTime;
    private String md5;

    public ClassFileDef(String className, String path, long lastModifyTime, byte[] data) {
        this.className = className;
        this.path = path;
        this.lastModifyTime = lastModifyTime;
        this.data = data;
      //  this.md5 = MD5Utils.bytesToMD5(data);
    }
}

