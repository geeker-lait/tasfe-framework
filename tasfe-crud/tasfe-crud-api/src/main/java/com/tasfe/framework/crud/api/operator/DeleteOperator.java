package com.tasfe.framework.crud.api.operator;

/**
 * Created by Lait on 2017/7/7.
 */
public interface DeleteOperator {

    /**
     * 根据id删除
     *
     * @param id
     */
    void del(Long id);

    /**
     * 根据指定的id集合删除
     *
     * @param ids
     */
    void dels(Long... ids);

    /**
     * 根据条件删除记录
     *
     * @param records
     * @return
     */
    <T> void del(T records) throws Exception;


}

