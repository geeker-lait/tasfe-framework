package com.tasfe.framework.crud.api.operator;

import java.util.List;

/**
 * Created by Lait on 2017/7/7.
 */
public interface UpdateOperator {

    /**
     * 根据主键更新指定的对象
     *
     * @param entity
     */
    <Entity> void update(Entity entity) throws Exception;


    /**
     * 皮量更新指定的对象
     *
     * @param entityList
     */
    <Entity> void updates(List<Entity> entityList);


}
