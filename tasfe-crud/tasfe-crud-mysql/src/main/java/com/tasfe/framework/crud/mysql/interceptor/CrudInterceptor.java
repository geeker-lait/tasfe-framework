package com.tasfe.framework.crud.mysql.interceptor;

import java.lang.reflect.Field;

/**
 * Created by Lait on 2017/4/15
 * crud 拦截器
 */
public interface CrudInterceptor {
    /**
     * 插入拦截
     *
     * @param fields
     * @return
     */
    boolean beforeHandler(Field[] fields, Object obj);

    /**
     * 修改拦截
     *
     * @param fields
     * @return
     */
    boolean afterHandler(Field[] fields, Object obj);
}
