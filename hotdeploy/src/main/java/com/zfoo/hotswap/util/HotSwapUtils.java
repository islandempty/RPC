package com.zfoo.hotswap.util;

import com.zfoo.protocol.exception.ExceptionUtils;
import com.zfoo.protocol.util.IOUtils;
import com.zfoo.protocol.util.StringUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.ClassFile;
import javassist.util.HotSwapAgent;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;

/**
 * @author islandempty
 * @since 2021/6/16
 **/
public abstract class HotSwapUtils {
    private static final Logger logger = LoggerFactory.getLogger(HotSwapUtils.class);

    //通过字节数组获取文件名
    private static String readClassName(byte[] bytes){

        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream((new ByteArrayInputStream(bytes)));
            var classfile = new ClassFile(dataInputStream);
            return  classfile.getName().replaceAll(StringUtils.SLASH,StringUtils.PERIOD_REGEX);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeIO(dataInputStream);
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
            logger.error(ExceptionUtils.getMessage(e));
        }

        hotswapClassByJavassist(clazz, bytes);
    }

    private static void hotswapClassByJavassist(Class<?> clazz, byte[] bytes){
        ByteArrayInputStream byteArrayInputStream = null;
        CtClass ctClass = null;
        try {
            clazz = Class.forName(readClassName(bytes));
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            ctClass = ClassPool.getDefault().makeClass(byteArrayInputStream);
            //Javassist热更新
            HotSwapAgent.redefine(clazz , ctClass);
            logger.info("Javassist热更新[{}]成功",clazz);
        } catch (Throwable t) {
            logger.info("无法使用javassist热更新，开始使用备用方案Byte Buddy做热更新",t);
            hotswapClassByByteBuddy(clazz,bytes);
        }finally {
            IOUtils.closeIO(byteArrayInputStream);
             /*
            如果一个CtClass对象通过writeFile()，toClass()或者toBytecode()转换成了class文件，那么Javassist会冻结这个CtClass对象。
            后面就不能继续修改这个CtClass对象了。这样是为了警告开发者不要修改已经被JVM加载的class文件，因为JVM不允许重新加载一个类。
             */
            if (ctClass!=null){
                ctClass.defrost();
            }
        }
    }

    private static void hotswapClassByByteBuddy(Class<?> clazz, byte[] bytes){
        //byte Buddy热更新
        var instrumentation = ByteBuddyAgent.install();
        try {
            instrumentation.redefineClasses(new ClassDefinition(clazz,bytes));
            logger.info("Byte Buddy热更新[{}]成功", clazz);
        } catch (Throwable t) {
            logger.error("Byte Buddy热更新未知异常，热更新[{}]失败", clazz);
        }
    }


}

