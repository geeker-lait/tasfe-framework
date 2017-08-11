package com.tasfe.framework.crud.mysql.impls.jpa.genericdao.mybatis.annotation;

import java.lang.annotation.*;

/**
 * mybatis中Dao层接口的标识，只有带有这个标识的接口才会被mybatis自动代理和实现
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MybatisRepository {

}
