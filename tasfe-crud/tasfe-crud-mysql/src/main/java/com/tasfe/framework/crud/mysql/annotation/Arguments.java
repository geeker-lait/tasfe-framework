package com.tasfe.framework.crud.mysql.annotation;

import java.lang.annotation.*;

/**
 * Created by Lait on 2017/4/15
 * 规则： 1. [注释标签参数]必须和[方法参数]，保持顺序一致 2. [注释标签参数]的参数数目不能大于[方法参数]的参数数目 3.
 * 只有在[注释标签参数]标注的参数，才会传递到SQL模板里 4. 如果[方法参数]只有一个，如果用户不设置 [注释标签参数]，则默认参数名为miniDto
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Arguments {

    String[] value() default {};
}
