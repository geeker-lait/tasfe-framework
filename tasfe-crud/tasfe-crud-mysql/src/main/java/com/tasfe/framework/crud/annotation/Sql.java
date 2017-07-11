package com.tasfe.framework.crud.annotation;

import java.lang.annotation.*;

/**
 * Created by Lait on 2017/4/15
 * SQL标签(记录用户自定义SQL模板)
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Sql {
    String value();
}
