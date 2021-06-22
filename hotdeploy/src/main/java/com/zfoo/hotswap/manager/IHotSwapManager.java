package com.zfoo.hotswap.manager;


import com.zfoo.hotswap.model.ClassFileDef;

import java.util.Map;

/**
 * @author islandempty
 * @since 2021/6/1
 **/
public interface IHotSwapManager {

    Map<String , ClassFileDef> getClassFileDefMap();

}
