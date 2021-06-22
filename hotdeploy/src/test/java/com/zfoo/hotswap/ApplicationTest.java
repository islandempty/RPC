package com.zfoo.hotswap;

import com.zfoo.hotswap.util.HotSwapUtils;
import com.zfoo.protocol.util.ClassUtils;
import com.zfoo.protocol.util.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

/**
 * @author islandempty
 * @since 2021/6/21
 **/


@Ignore
public class ApplicationTest {

    // 使用Javassist热更新更加的轻量，如果Javassist热更新失败，则会自动使用Byte Buddy做热更新

    @Test
    public void startHotSwapTest() throws IOException{
        //热更新限制，不能为需要热更新的类添加或减少成员函数和成员变量，只能修改函数内部的逻辑
        HotswapClass test = new HotswapClass();
        HotswapClass1 test1 = new HotswapClass1();
        //没有热更新输出
        test.print();
        test1.print();
        //随便修改print方法，然后编译成为一个需要热更新的class文件
        HotSwapUtils.hotswapClass(IOUtils.toByteArray(ClassUtils.getFileFromClassPath("HotswapClass.class")));
        HotSwapUtils.hotswapClass(IOUtils.toByteArray(ClassUtils.getFileFromClassPath("HotswapClass1.class")));
        //热更新之后的输出
        test.print();
        test1.print();

    }
}

