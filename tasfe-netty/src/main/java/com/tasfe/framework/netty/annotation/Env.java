package com.tasfe.framework.netty.annotation;

import java.lang.annotation.*;

/**
 * Created by Lait on 2017/6/9.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Env {
    String profile();
}
