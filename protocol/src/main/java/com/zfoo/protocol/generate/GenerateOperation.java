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
    public static final GenerateOperation NO_OPERATION = new GenerateOperation();

    /**
     * 折叠协议，生成协议文件会和Java源文件保持相同的目录结构
     */
    private boolean foldProtocol;

    /**
     * 生成协议文件的后缀名称，如果不指定，用语言约定的默认名称
     */
    private String protocolParam;

    /**
     * 生成javascript协议文件
     */
    private boolean generateJsProtocol;

    /**
     * 生成C#协议文件
     */
    private boolean generateCsharpProtocol;

    /**
     * 生成Lua协议文件
     */
    private boolean generateLuaProtocol;

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

    public boolean isGenerateJsProtocol() {
        return generateJsProtocol;
    }

    public void setGenerateJsProtocol(boolean generateJsProtocol) {
        this.generateJsProtocol = generateJsProtocol;
    }

    public boolean isGenerateCsharpProtocol() {
        return generateCsharpProtocol;
    }

    public void setGenerateCsharpProtocol(boolean generateCsharpProtocol) {
        this.generateCsharpProtocol = generateCsharpProtocol;
    }

    public boolean isGenerateLuaProtocol() {
        return generateLuaProtocol;
    }

    public void setGenerateLuaProtocol(boolean generateLuaProtocol) {
        this.generateLuaProtocol = generateLuaProtocol;
    }
}

