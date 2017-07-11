package com.tasfe.framework.crud.api;

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
    <T> void insert(T record) throws Exception;

    /**
     * 批量，不返回主键
     * @param records
     * @return 影响的记录数
     */
    //void batchInsert(List<?> records) throws Exception;


    /**
     * 批量增加,并返回主键,主键在对象中
     * @param records
     * @return
     */
    <T> void insertBatch(List<T> records) throws Exception;

}
