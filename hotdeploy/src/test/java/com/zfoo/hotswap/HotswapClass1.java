package com.zfoo.hotswap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author islandempty
 * @since 2021/6/21
 **/
public class HotswapClass1 {

    private static final Logger logger = LoggerFactory.getLogger(HotswapClass1.class);

    public void print(){
        System.out.println("热更新开始之前");
    }
}

