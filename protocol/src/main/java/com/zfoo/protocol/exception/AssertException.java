package com.zfoo.protocol.exception;

import com.zfoo.protocol.util.StringUtils;

/**
 * @author islandempty
 * @since 2021/6/2
 **/
public class AssertException extends RuntimeException{
        public AssertException(String message){
            super(message);
        }

    /**
     * 格式化字符串
     * 此方法只是简单将占位符 {} 按照顺序替换为参数
     * 例如:format("this is {} for {}", "a", "b") =》 this is a for b
     *
     * @param template 字符串模板
     * @param args 参数列表
     */
        public AssertException(String template,Object... args){
            super(StringUtils.format(template,args));
        }

}

