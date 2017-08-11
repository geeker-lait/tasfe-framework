package com.tasfe.framework.crud.api.annotation;

import java.lang.annotation.*;

/**
 * Created by Lait on 2017/8/4.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Sharding {
    String key();
}
