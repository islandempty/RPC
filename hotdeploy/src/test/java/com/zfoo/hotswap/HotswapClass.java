package com.zfoo.hotswap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author islandempty
 * @since 2021/6/21
 **/
public class HotswapClass {

    private static final Logger logger = LoggerFactory.getLogger(HotswapClass.class);

    public void print(){
        logger.info("热更新的内容");
    }
}

