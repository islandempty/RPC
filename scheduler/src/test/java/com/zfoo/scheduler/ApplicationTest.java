package com.zfoo.scheduler;

import com.ie.util.ThreadUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author islandempty
 * @since 2021/7/4
 **/

@Ignore
public class ApplicationTest {

    @Test
    public void startSchedulerTest(){
        //加载配置文件，配置文件中必须引入scheduler
        var context = new ClassPathXmlApplicationContext("application.xml");

        ThreadUtils.sleep(Long.MAX_VALUE);
    }
}

