package com.tasfe.framework.crud.mysql.business;

import com.tasfe.framework.crud.api.params.CrudParam;

import java.util.List;
import java.util.Map;

/**
 * Created by Lait on 2017/4/15
 * 通用Crud 数据访问层接口
 */
public interface CrudBusiness {

    /**
     * 根据主键查询
     *
     * @param <T>          pojo类
     * @param clazz        pojo类-class对象
     * @param primaryValue 主键值
     * @return pojo对象
     * @throws Exception
     */
    <T> T selectByPrimaryKey(Class<T> clazz, Long primaryValue) throws Exception;

    /**
     * 插入数据
     * <p>
     * 实际调用insertSelective
     * </p>
     *
     * @param <T> pojo类
     * @param t   pojo对象
     * @return 数据条数
     * @throws Exception
     */
    <T> int insert(T t) throws Exception;

    /**
     * 插入数据
     *
     * @param <T> pojo类
     * @param t   pojo对象
     * @return 数据条数
     * @throws Exception
     */
    <T> int insertSelective(T t) throws Exception;

    <T> int insertBatch(List<T> list) throws Exception;

    /**
     * 删除
     * <p>
     * 根据主键删除
     * </p>
     *
     * @param <T>          pojo类
     * @param clazz        pojo类-class对象
     * @param primaryValue 主键值
     * @return 数据条数
     */
    <T> int deleteByPrimaryKey(Class<T> clazz, String primaryValue);

    /**
     * 删除
     * <p>
     * 根据条件删除
     * </p>
     *
     * @param <T>            pojo类
     * @param clazz          pojo类-class对象
     * @param conditionExp   查询条件 where 表达式 @see CrudParam
     * @param conditionParam 查询条件 where 表达式中的参数集 @see CrudParam
     * @return 数据条数
     */
    <T> int deleteByCondition(Class<T> clazz, String conditionExp, Map<String, Object> conditionParam);

    /**
     * 更新
     * <p>
     * 根据主键更新
     * </p>
     * <p>
     * 更新pojo的所有字段，包括空值(null值)字段
     * </p>
     *
     * @param <T> pojo类
     * @param t   pojo对象
     * @return 数据条数
     * @throws Exception
     */
    <T> int updateByPrimaryKey(T t) throws Exception;

    /**
     * 更新
     * <p>
     * 根据主键更新
     * </p>
     * <p>
     * 更新pojo的非空字段
     * </p>
     *
     * @param <T> pojo类
     * @param t   pojo对象
     * @return 数据条数
     * @throws Exception
     */
    <T> int updateByPrimaryKeySelective(T t) throws Exception;

    /**
     * 更新
     * <p>
     * 根据条件更新
     * </p>
     * <p>
     * 更新pojo的指定字段集
     * </p>
     *
     * @param clazz              pojo类-class对象
     * @param columnValueMapping 需要更新的列名-值,注意列名必须与数据库中列名一致
     * @param conditionExp       查询条件 where 表达式 @see CrudParam
     * @param conditionParam     查询条件 where 表达式中的参数集 @see CrudParam
     * @return 数据条数
     * @throws Exception
     */
    <T> int updateByConditionSelective(Class<T> clazz, Map<String, Object> columnValueMapping, String conditionExp,
                                       Map<String, Object> conditionParam) throws Exception;

    /**
     * 高级查询
     *
     * @param clazz      pojo类-class对象
     * @param crudParam 查询参数
     * @return
     * @throws Exception
     */
    <T> List<T> selectAdvanced(Class<T> clazz, CrudParam crudParam) throws Exception;

    /**
     * 高级查询,指定返回列
     *
     * @param clazz      pojo类-class对象
     * @param crudParam 查询参数
     * @return
     * @throws Exception
     */
    <T> List<Map<String, Object>> selectAdvancedByColumn(Class<T> clazz, CrudParam crudParam) throws Exception;

}
