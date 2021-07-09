package com.zfoo.protocol.generate;

/**
 * 创建协议文件的操作类
 *
 * @author islandempty
 * @since 2021/7/8
 **/
public class GenerateOperation {
    /**
     * 不创建任何协议文件
     */
    public static final GenerateOperation NO_OPERATION =new GenerateOperation();

    /**
     * 折叠协议，生成协议文件会和Java源文件保持相同的目录结构
     */
    private boolean foldProtocol;

    /**
     * 生成协议文件的后缀名称，如果不指定，用语言约定的默认名称
     */
    private String protocolParam;

    public boolean isFoldProtocol() {
        return foldProtocol;
    }

    public void setFoldProtocol(boolean foldProtocol) {
        this.foldProtocol = foldProtocol;
    }

    public String getProtocolParam() {
        return protocolParam;
    }

    public void setProtocolParam(String protocolParam) {
        this.protocolParam = protocolParam;
    }
}

