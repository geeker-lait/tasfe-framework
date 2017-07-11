package com.tasfe.framework.crud.mysql.annotation;

import java.lang.annotation.*;

/**
 * Created by Lait on 2017/4/15
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    String value();
}  