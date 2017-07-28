package com.tasfe.framework.crud.api;

import com.tasfe.framework.crud.api.dto.QueryParam;

import java.util.List;
import java.util.Map;

/**
 * Created by Lait on 2017/7/10.
 */
public interface Crudable {
    /**
     * 单插入
     */
    String IN = "_in";
    /**
     * 批插入
     */
    String INS = "_ins";


    /**
     * 获取单条,id查询
     */
    String GET = "_get";
    /**
     * 获取一条或多条,equals条件查询
     */
    String GETS = "_gets";

    /**
     * 获取一条或多条,like条件查询
     */
    String FIND = "_find";

    /**
     * 关联或复杂条件查询
     */
    String QUERY = "_query";


    /**
     * 单条更新
     */
    String UPD = "_upd";

    /**
     * 单条或多条更新
     */
    String UPDS = "_upds";


    /**
     * 单挑删除,id删除
     */
    String DEL = "_del";
    /**
     * 单挑或多条删除，条件删除
     */
    String DELS = "_dels";

    /**
     * 条数
     */
    String COUNT = "_count";

    /**
     * 获取最大值
     */
    String MAX = "_max";

    /**
     * 获取最小值
     */
    String MIN = "_min";

    /**
     * 获取平均值
     */
    String AVG = "_avg";

    /**
     * 获取存储器的名字
     *
     * @return
     */
    String getStoragerName();


    /*********************************** insert ***********************************/
    /**
     * 插入数据
     * @param <T> pojo类
     * @param t   pojo对象
     * @return 数据条数
     * @throws Exception
     */
    <T> void _in(T t) throws Exception;

    <T> void _ins(List<T> list) throws Exception;

    /*********************************** get/find/query ***********************************/

    /**
     * 根据主键查询
     *
     * @param <T>   pojo类
     * @param clazz pojo类-class对象
     * @param pk    主键值
     * @return pojo对象
     * @throws Exception
     */
    <T> T _get(Class<T> clazz, Long pk) throws Exception;

    /**
     * 高级查询
     *
     * @param clazz      pojo类-class对象
     * @param queryParam 查询参数
     * @return
     * @throws Exception
     */
    <T> List<T> _gets(Class<T> clazz, QueryParam queryParam) throws Exception;



    /**
     * 高级查询
     *
     * @param clazz      pojo类-class对象
     * @param queryParam 查询参数
     * @return
     * @throws Exception
     */
    <T> List<T> _find(Class<T> clazz, QueryParam queryParam) throws Exception;

    /**
     * 高级查询,指定返回列
     *
     * @param clazz      pojo类-class对象
     * @param queryParam 查询参数
     * @return
     * @throws Exception
     */
    <T> List<Map<String, Object>> _query(Class<T> clazz, QueryParam queryParam) throws Exception;

    /*********************************** delete ***********************************/
    /**
     * 删除
     * <p>
     * 根据主键删除
     * </p>
     *
     * @param <T>          pojo类
     * @param queryParam        pojo类-class对象
     * @return 数据条数
     */
    <T> void _del(QueryParam queryParam) throws Exception;
    /**
     * 删除
     * <p>
     * 根据主键删除
     * </p>
     *
     * @param <T>          pojo类
     * @param clazz        pojo类-class对象
     * @param pk 主键值
     * @return 数据条数
     */
    <T> void _del(Class<T> clazz, Long pk);

    /**
     * 删除
     * <p>
     * 根据条件删除
     * </p>
     *
     * @param <T>            pojo类
     * @param clazz          pojo类-class对象
     * @param conditionExp   查询条件 where 表达式 @see QueryParam
     * @param conditionParam 查询条件 where 表达式中的参数集 @see QueryParam
     * @return 数据条数
     */
    <T> int _dels(Class<T> clazz, String conditionExp, Map<String, Object> conditionParam);



    /*********************************** update ***********************************/
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
    <T> int _upd(T t) throws Exception;

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
     * @param conditionExp       查询条件 where 表达式 @see QueryParam
     * @param conditionParam     查询条件 where 表达式中的参数集 @see QueryParam
     * @return 数据条数
     * @throws Exception
     */
    <T> int _upds(Class<T> clazz, Map<String, Object> columnValueMapping, String conditionExp,
                                            Map<String, Object> conditionParam) throws Exception;


    /**
     * 高级查询
     *
     * @param clazz      pojo类-class对象
     * @param queryParam 查询参数
     * @return
     * @throws Exception
     */
    <T> Long _count(Class<T> clazz, QueryParam queryParam) throws Exception;


    /**
     * 获取最大数
     * @param queryParam
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> Number _max(QueryParam queryParam)throws Exception;



    /**
     * 获取最大数
     * @param queryParam
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> Number _min(QueryParam queryParam)throws Exception;


    /**
     * 获取最大数
     * @param queryParam
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> Number _avg(QueryParam queryParam)throws Exception;


    /**
     * 获取最大数
     * @param queryParam
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> Number _sum(QueryParam queryParam)throws Exception;


}
