package com.tasfe.framework.crud.api.operator;

/**
 * Created by Lait on 2017/8/6.
 */
public interface FunctionOperator {

    /**
     * 根据条件获取查询的总记录数
     *
     * @param entity 查询条件参数
     * @return 总记录数
     */
    <Entity> Long counts(Entity entity) throws Exception;

    //max


    //min


    //avg


    //
}
