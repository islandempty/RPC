package com.ie.protocol.exception;

import com.ie.protocol.util.FileUtils;
import com.ie.protocol.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * @author islandempty
 * @since 2021/6/17
 **/
public class ExceptionUtils {

    /**
     * 获取异常全部信息，格式是：
     * <p>
     * 类名称: 异常信息
     * 异常堆栈
     * </p>
     *
     * @param throwable the throwable to get a message for, null returns empty string
     * @return 异常的信息
     */
    public static String getMessage(final Throwable throwable){
        if (throwable == null){
            return StringUtils.EMPTY;
        }
        final  String className = throwable.getClass().getName();
        return className+":" +throwable.getMessage() + FileUtils.LS+getStackTrace(throwable);
    }

    //JUnit源码

    /**
     * <p>Gets the stack trace from a Throwable as a String.</p>
     * <p>The result of this method vary by JDK version as this method uses {@link Throwable#printStackTrace(java.io.PrintWriter)}.
     * @param throwable
     * @return
     */
    public static String getStackTrace(final Throwable throwable){
        final StringWriter sw = new StringWriter();
        final PrintWriter pw =new PrintWriter(sw,true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    //获取堆栈信息
    public static String getCurrentStackTrace(){
        var builder = new StringBuilder();
        var stackTraces = Thread.currentThread().getStackTrace();
        Arrays.stream(stackTraces).forEach(it -> builder.append(it.toString()).append(FileUtils.LS));
        return builder.toString();
    }
}

