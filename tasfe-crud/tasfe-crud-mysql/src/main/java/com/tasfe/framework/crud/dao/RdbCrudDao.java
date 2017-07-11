package com.tasfe.framework.crud.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by Lait on 2017/4/15
 * crud通用dao
 */
public interface RdbCrudDao{

    Map<String, Object> selectByPrimaryKey(Map<String, Object> param);

    int deleteByPrimaryKey(Map<String, Object> param);

    int deleteByCondition(Map<String, Object> param);

    //@InsertProvider(type = CrudDaoProvider.class, method = "insertSelectiveSql")
    int insert(Map<String, Object> param);

    int insertSelective(Map<String, Object> param);

    int insertBatch(Map<String, Object> param);

    int batchInsert(List<Object> entitys);

    int updateByPrimaryKeySelective(Map<String, Object> param);

    int updateByPrimaryKey(Map<String, Object> param);

    int updateByConditionSelective(Map<String, Object> param);

    List<Map<String, Object>> selectAdvanced(Map<String, Object> param);

}
