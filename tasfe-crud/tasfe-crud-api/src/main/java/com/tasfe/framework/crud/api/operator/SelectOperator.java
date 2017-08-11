package com.tasfe.framework.crud.api.operator;

import com.tasfe.framework.crud.api.params.Criteria;

import java.util.List;

/**
 * Created by Lait on 2017/7/7.
 */
public interface SelectOperator{

    /**
     * 根据id获取
     *
     * @param id
     * @return
     */
    <Entity> Entity get(Class<Entity> entity,Long id) throws Exception;

    /**
     * 根据指定的id集合查询
     *
     * @param ids
     * @return
     */
    <Entity> List<Entity> gets(Class<Entity> entity,Long... ids) throws Exception;


    /**
     * 使用equal查询
     *
     * @param entity
     * @return
     */
    <Entity> List<Entity> gets(Entity entity) throws Exception;

    /**
     * 使用like 查询
     *
     * @param entity
     * @return
     */
    <Entity> List<Entity> find(Entity entity,Criteria criteria) throws Exception;



}
