package com.tasfe.framework.crud.mysql;

import com.tasfe.framework.crud.api.Crudable;
import com.tasfe.framework.crud.api.enums.StoragerType;
import com.tasfe.framework.crud.api.params.Criteria;
import com.tasfe.framework.crud.api.params.CrudParam;
import com.tasfe.framework.crud.api.params.Kvc;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.framework.crud.api.operator.mysql.RdbOperator;
import com.tasfe.framework.crud.core.utils.FieldReflectUtil;
import com.tasfe.framework.crud.core.utils.GeneralMapperReflectUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lait on 2017/5/29.
 * mysql实现
 */
@Component("mysql")
public class MysqlTemplate extends CrudTemplate implements RdbOperator, InitializingBean {
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    private SqlSession sqlSession;


    /********************************** in *************************************/
    @Override
    public <T> void _in(T t) throws Exception {
        Map<String, Object> param = new HashMap<>();
        Class<?> clazz = t.getClass();
        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
        Map<String, String> mapping = GeneralMapperReflectUtil.getFieldValueMappingExceptNull(t,false);
        param.put("tableName", tableName);
        param.put("columnValueMapping", mapping);
        sqlSession.update(Crudable.IN, param);
        Field field = FieldReflectUtil.findField(t.getClass(),"id");
        if(null != field){
            FieldReflectUtil.setFieldValue(t,field,Long.valueOf(param.get("id").toString()));
        }
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


    /********************************** upd *************************************/
    @Override
    public <T> int _upd(T t) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        Class<?> clazz = t.getClass();

        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
        String primaryKey = GeneralMapperReflectUtil.getPrimaryKey(clazz);

        Map<String, String> mapping = GeneralMapperReflectUtil.getFieldValueMappingExceptNull(t,false);

        String primaryValue = mapping.get(primaryKey);

        mapping.remove(primaryKey);

        param.put("tableName", tableName);
        param.put("primaryKey", primaryKey);
        param.put("primaryValue", primaryValue);
        param.put("columnValueMapping", mapping);

        return sqlSession.update(Crudable.UPD,param);
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



    /********************************** get *************************************/

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
    public <T> List<T> _gets(Class<T> clazz, CrudParam crudParam) throws Exception {
        Map<String, Object> param = new HashMap<>();
        String tableName = GeneralMapperReflectUtil.getTableName(clazz);

        param.put("tableName", tableName);
        param.put("queryColumn", null == crudParam.getQueryColumn()?GeneralMapperReflectUtil.getAllColumns(clazz,false): crudParam.getQueryColumn());
        Object entity = crudParam.getEntity();
        if(entity!=null){
            param.put("conditionParam", null == crudParam.getConditionParam()?GeneralMapperReflectUtil.getFieldValueMappingExceptNull(entity,false): crudParam.getConditionParam());
        }

        param.put("conditionExp", crudParam.getConditionExp());
        param.put("orderExp", crudParam.getOrderExp());
        param.put("pks", crudParam.getPks());
        //param.put("",);

        if (crudParam.getPageSize() != null && crudParam.getPageNo() != null) {
            Map<String, Integer> page = new HashMap<>();
            page.put("pageSize", crudParam.getPageSize());
            page.put("startRowNo", (crudParam.getPageNo() - 1) * crudParam.getPageSize());
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
    public <T> List<T> _find(Class<T> clazz, Criteria criteria) throws Exception {
        List<T> result = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        String tableName = GeneralMapperReflectUtil.getTableName(criteria.getClazz());

        param.put("table", tableName);

        param.put("columns", criteria.getSelects().size() == 0 ? GeneralMapperReflectUtil.getAllColumns(clazz,false): criteria.getSelects());

        param.put("whereExp",criteria.getWhere().getKvcs());

        param.put("orderExp",criteria.getOrder());

        param.put("limit",criteria.getLimit());

        sqlSession.selectList(Crudable.FIND,param);



        /*List<Map<String, Object>> list = mysqlSelectAdvancedByColumn(clazz, crudParam);

        if (list != null && list.size() != 0) {
            for (Map<String, Object> mapping : list) {
                result.add(GeneralMapperReflectUtil.parseToBean(mapping, clazz));
            }
        }*/
        return result;
    }

    @Override
    public <T> List<Map<String, Object>> _query(Class<T> clazz, CrudParam crudParam) throws Exception {
        return null;
    }




    /********************************** del *************************************/


    @Override
    public <T> void _del(CrudParam crudParam) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        Class _class = crudParam.getEntity().getClass();
        String tableName = GeneralMapperReflectUtil.getTableName(_class);
        String primaryKey = GeneralMapperReflectUtil.getPrimaryKey(_class);

        param.put("tableName", tableName);
        param.put("primaryKey", primaryKey);
        Field field = FieldReflectUtil.findField(_class,"id");
        Long pk = -1L;
        if(null != field){
            pk = (Long) FieldReflectUtil.getFieldValue(crudParam.getEntity(),field);
        }
        param.put("primaryValue", crudParam.getPk()==null?pk: crudParam.getPk());
        sqlSession.delete(Crudable.DEL,param);
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



    /********************************** funs *************************************/
    @Override
    public <T> Long _count(Class<T> clazz, CrudParam crudParam) throws Exception {
        Map<String, Object> param = new HashMap<>();
        String tableName = GeneralMapperReflectUtil.getTableName(clazz);

        param.put("tableName", tableName);
        param.put("queryColumn", null == crudParam.getQueryColumn()?GeneralMapperReflectUtil.getAllColumns(clazz,false): crudParam.getQueryColumn());
        Object entity = crudParam.getEntity();
        if(entity!=null){
            param.put("conditionParam", null == crudParam.getConditionParam()?GeneralMapperReflectUtil.getFieldValueMappingExceptNull(entity,false): crudParam.getConditionParam());
        }

        param.put("conditionExp", crudParam.getConditionExp());
        param.put("pks", crudParam.getPks());

        Long totalSize = sqlSession.selectOne(Crudable.COUNT,param);
        return totalSize;
    }

    @Override
    public <T> Number _max(CrudParam crudParam) throws Exception {
        return null;
    }

    @Override
    public <T> Number _min(CrudParam crudParam) throws Exception {
        return null;
    }

    @Override
    public <T> Number _avg(CrudParam crudParam) throws Exception {
        return null;
    }

    @Override
    public <T> Number _sum(CrudParam crudParam) throws Exception {
        return null;
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
