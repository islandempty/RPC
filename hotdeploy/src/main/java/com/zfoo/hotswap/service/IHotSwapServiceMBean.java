package com.zfoo.hotswap.service;


import javax.management.MXBean;

@MXBean
public interface IHotSwapServiceMBean {

    /**
     *  热更新相对路径的文件
     * @param relativePath 相对路径
     */
    void hotSwapByRelativePath(String relativePath);

    /**
     *热更新绝对路径的文件
     * @param absolutePath 绝对路径
     */
    void hotSwapByAbsolutePath(String absolutePath);

    void logAllUpdateClassFileInfo();
}
