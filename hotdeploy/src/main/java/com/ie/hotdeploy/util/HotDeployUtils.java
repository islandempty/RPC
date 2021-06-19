package com.ie.hotdeploy.util;

import com.ie.protocol.util.StringUtils;
import javassist.bytecode.ClassFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author islandempty
 * @since 2021/6/16
 **/
public abstract class HotDeployUtils {
    private static final Logger logger = LoggerFactory.getLogger(HotDeployUtils.class);

    //
    private static String readClassName(byte[] bytes){
        ByteArrayInputStream byteArrayInputStream = null;
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream((new ByteArrayInputStream(bytes)));
            var classfile = new ClassFile(dataInputStream);
            return  classfile.getName().replaceAll(StringUtils.SLASH,StringUtils.PERIOD_REGEX);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 热更新java文件
     * jvm的启动参数，jdk11过后默认的全款更不允许连接自己，VM: -Djdk.attach.allowAttachSelf=true
     * <p>
     * 优先使用简单的Javassist做热更新，因为Byte Buddy使用了更为复杂的ASM，spring boot中会使用Byte Buddy热更新
     *
     * @param bytes .class结尾的字节码文件
     */
    public static synchronized void hotswapClass(byte[] bytes){
        if (bytes == null || bytes.length<=0){
            return;
        }

        Class<?> clazz=null;

        try {
             clazz = Class.forName(readClassName(bytes));
        } catch (ClassNotFoundException e) {
            //logger.error();
        }
    }

}

