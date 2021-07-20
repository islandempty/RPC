package com.zfoo.net.dispatcher.model.anno;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PacketReceiver {
}
