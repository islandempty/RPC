package com.zfoo.scheduler.model.anno;

import java.lang.annotation.*;

/**
 * @author islandempty
 * @since 2021/7/1
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Scheduler {

    String cron();
}

