package com.zfoo.storage.model.anno;

import java.lang.annotation.*;

/**
 * @author islandempty
 * @since 2021/6/23
 **/

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
}

