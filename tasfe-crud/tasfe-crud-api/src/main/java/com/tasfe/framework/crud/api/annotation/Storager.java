package com.tasfe.framework.crud.api.annotation;

import com.tasfe.framework.crud.api.enums.StoragerType;

import java.lang.annotation.*;

/**
 * Created by Lait on 2017/7/10.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Storager {
    /**
     * 提供根据对像指定的存储策略进行存储到指定类型的数据库中，如若没有设置，默认使用mysql，可设置多个(数据会双落)
     * @see StoragerType
     */
    StoragerType[] storage() default {StoragerType.MYSQL};

}
