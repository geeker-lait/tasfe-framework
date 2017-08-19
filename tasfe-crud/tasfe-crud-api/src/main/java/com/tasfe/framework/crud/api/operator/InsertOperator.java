package com.tasfe.framework.crud.api.operator;

import java.util.List;

/**
 * Created by Lait on 2017/7/7.
 */
public interface InsertOperator {
    /**
     * 插入一条数据，忽略record中的ID
     *
     * @param record
     * @return 影响的记录数
     */
    <T> void save(T record) throws Exception;

    /**
     * 批量增加/更新,并返回主键,主键在对象中
     * @param record
     * @return
     */
    <T> void saveOrUpd(T record) throws Exception;

    /**
     * 批量增加,并返回主键,主键在对象中
     * @param records
     * @return
     */
    <T> void save(List<T> records) throws Exception;


    /**
     * 批量增加/更新,并返回主键,主键在对象中
     * @param records
     * @return
     */
    <T> void saveOrUpds(List<T> records) throws Exception;

}
