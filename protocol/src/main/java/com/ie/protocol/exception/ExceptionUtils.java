package com.ie.protocol.exception;

import com.ie.protocol.util.StringUtils;

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
        //return className+":" +throwable.getMessage() + FileU
        return null;
    }
}

