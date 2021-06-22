package com.zfoo.hotswap;

import com.zfoo.hotswap.manager.HotSwapManager;
import com.zfoo.hotswap.service.HotSwapServiceMBean;

/**
 * @author islandempty
 * @since 2021/6/20
 **/
public class HotSwapContext {

    public static final HotSwapContext HOT_SWAP_CONTEXT = new HotSwapContext();

    private HotSwapContext(){
    }

    public static HotSwapServiceMBean getHotSwapService(){
        return HotSwapServiceMBean.getSingleInstance();
    }

    public static HotSwapManager getHotSwapManager(){
        return HotSwapManager.getInstance();
    }
}

