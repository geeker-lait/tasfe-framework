package com.tasfe.framework.crud;

import com.tasfe.framework.crud.api.Crudable;
import com.tasfe.framework.crud.api.StoragerType;
import com.tasfe.framework.crud.api.dto.QueryParam;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.framework.crud.api.provider.mysql.MysqlOperator;
import com.tasfe.framework.crud.utils.GeneralMapperReflectUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lait on 2017/5/29.
 * mysql实现
 */
@Component("mysql")
public class MysqlTemplate extends CrudTemplate implements MysqlOperator, InitializingBean {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    private SqlSession sqlSession;


    @Override
    public <T> void _in(T t) throws Exception {
        Map<String, Object> param = new HashMap<>();
        Class<?> clazz = t.getClass();
        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
        Map<String, String> mapping = GeneralMapperReflectUtil.getFieldValueMappingExceptNull(t,false);
        param.put("tableName", tableName);
        param.put("columnValueMapping", mapping);
        sqlSession.update(Crudable.IN, param);
    }

    @Override
    public <T> void _ins(List<T> list) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        String tableName = "";
        List<String> columns = new ArrayList<String>();

        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

        for (T t : list) {
            if (tableName.equals("")) {
                Class<?> clazz = t.getClass();
                tableName = GeneralMapperReflectUtil.getTableName(clazz);
            }
            if (columns.size() == 0) {
                Class<?> clazz = t.getClass();
                columns = GeneralMapperReflectUtil.getAllColumns(clazz,false);
            }
            Map<String, String> mapping = GeneralMapperReflectUtil.getAllFieldValueMapping(t);
            dataList.add(mapping);
        }

        param.put("tableName", tableName);
        param.put("columns", columns);
        param.put("dataList", dataList);

        sqlSession.update(Crudable.INS, param);
    }


    @Override
    public <T> T _get(Class<T> clazz, Long pk) throws Exception {
        Map<String, Object> param = new HashMap<>();

        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
        String primaryKey = GeneralMapperReflectUtil.getPrimaryKey(clazz);
        List<String> queryColumn = GeneralMapperReflectUtil.getAllColumns(clazz,false);

        param.put("tableName", tableName);
        param.put("primaryKey", primaryKey);
        param.put("primaryValue", pk);
        param.put("queryColumn", queryColumn);
        Map map = sqlSession.selectOne(Crudable.GET, param);
        T t = clazz.newInstance();
        BeanUtils.populate(t,map);
        return t;
    }

    @Override
    public <T> List<T> _gets(Class<T> clazz, QueryParam queryParam) throws Exception {
        Map<String, Object> param = new HashMap<>();
        String tableName = GeneralMapperReflectUtil.getTableName(clazz);

        param.put("tableName", tableName);
        param.put("queryColumn", null == queryParam.getQueryColumn()?GeneralMapperReflectUtil.getAllColumns(clazz,false):queryParam.getQueryColumn());
        Object entity = queryParam.getEntity();
        if(entity!=null){


        }

        param.put("conditionExp", queryParam.getConditionExp());
        param.put("conditionParam", queryParam.getConditionParam());
        param.put("orderExp", queryParam.getOrderExp());
        param.put("pks", queryParam.getPks());
        //param.put("",);

        if (queryParam.getPageSize() != null && queryParam.getPageNo() != null) {
            Map<String, Integer> page = new HashMap<>();
            page.put("pageSize", queryParam.getPageSize());
            page.put("startRowNo", (queryParam.getPageNo() - 1) * queryParam.getPageSize());
            param.put("page", page);
        }

        List<Map> maps =  sqlSession.selectList(Crudable.GETS,param);
        List<T> ts = new ArrayList<>();
        for(Map map :maps){
            T t = clazz.newInstance();
            BeanUtils.populate(t,map);
            ts.add(t);
        }
        return ts;
    }

    @Override
    public <T> List<T> _find(Class<T> clazz, QueryParam queryParam) throws Exception {
        List<T> result = new ArrayList<>();

        queryParam.setQueryColumn(GeneralMapperReflectUtil.getAllColumns(clazz,false));

        /*List<Map<String, Object>> list = mysqlSelectAdvancedByColumn(clazz, queryParam);

        if (list != null && list.size() != 0) {
            for (Map<String, Object> mapping : list) {
                result.add(GeneralMapperReflectUtil.parseToBean(mapping, clazz));
            }
        }*/
        return result;
    }

    @Override
    public <T> List<Map<String, Object>> _query(Class<T> clazz, QueryParam queryParam) throws Exception {
        return null;
    }

    @Override
    public <T> void _del(Class<T> clazz, Long pk) {
        Map<String, Object> param = new HashMap<String, Object>();

        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
        String primaryKey = GeneralMapperReflectUtil.getPrimaryKey(clazz);

        param.put("tableName", tableName);
        param.put("primaryKey", primaryKey);
        param.put("primaryValue", pk);

    }

    @Override
    public <T> int _dels(Class<T> clazz, String conditionExp, Map<String, Object> conditionParam) {
        Map<String, Object> param = new HashMap<String, Object>();

        String tableName = GeneralMapperReflectUtil.getTableName(clazz);

        param.put("tableName", tableName);
        param.put("conditionExp", conditionExp);
        param.put("conditionParam", conditionParam);

        return 0;
    }

    @Override
    public <T> int _upd(T t) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        Class<?> clazz = t.getClass();

        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
        String primaryKey = GeneralMapperReflectUtil.getPrimaryKey(clazz);

        Map<String, String> mapping = GeneralMapperReflectUtil.getAllFieldValueMapping(t);

        String primaryValue = mapping.get(primaryKey);

        mapping.remove(primaryKey);

        param.put("tableName", tableName);
        param.put("primaryKey", primaryKey);
        param.put("primaryValue", primaryValue);
        param.put("columnValueMapping", mapping);
        return 0;
    }

    @Override
    public <T> int _upds(Class<T> clazz, Map<String, Object> columnValueMapping, String conditionExp, Map<String, Object> conditionParam) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        String tableName = GeneralMapperReflectUtil.getTableName(clazz);

        param.put("tableName", tableName);
        param.put("columnValueMapping", columnValueMapping);
        param.put("conditionExp", conditionExp);
        param.put("conditionParam", conditionParam);
        return 0;
    }

    public final void afterPropertiesSet() throws IllegalArgumentException, BeanInitializationException {
        try {
            sqlSession = new SqlSessionTemplate(sqlSessionFactory);
        } catch (Exception var2) {
            throw new BeanInitializationException("Initialization of DAO failed", var2);
        }
    }

    public SqlSession getSqlSession() {
        return this.sqlSession;
    }

    @Override
    public String getStoragerName() {
        return StoragerType.MYSQL.toString();
    }
}
