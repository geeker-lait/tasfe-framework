package com.tasfe.framework.crud.mysql.business.impls;

import org.springframework.stereotype.Service;

/**
 * Created by Lait on 2017/4/15
 * 通用Crud 数据访问层接口实现类
 */
@Service
public class CrudBusinessImpl/* implements CrudBusiness*/ {
//
//    //@Resource(name = "generalDao")
//    @Autowired
//    private RdbCrudDao dao;
//
//    @Override
//    public <T> T selectByPrimaryKey(Class<T> clazz, Long primaryValue) throws Exception {
//        Map<String, Object> param = new HashMap<String, Object>();
//
//        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
//        String primaryKey = GeneralMapperReflectUtil.getPrimaryKey(clazz);
//        List<String> queryColumn = GeneralMapperReflectUtil.getAllColumns(clazz);
//
//        param.put("tableName", tableName);
//        param.put("primaryKey", primaryKey);
//        param.put("primaryValue", primaryValue);
//        param.put("queryColumn", queryColumn);
//
//        return GeneralMapperReflectUtil.parseToBean(dao.selectByPrimaryKey(param), clazz);
//    }
//
//    @Override
//    public <T> int insertSelective(T t) throws Exception {
//        Map<String, Object> param = new HashMap<>();
//
//        Class<?> clazz = t.getClass();
//
//        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
//        Map<String, String> mapping = GeneralMapperReflectUtil.getFieldValueMappingExceptNull(t);
//
//        param.put("tableName", tableName);
//        param.put("columnValueMapping", mapping);
//
//        return dao.insertSelective(param);
//    }
//
//    @Override
//    public <T> int insert(T t) throws Exception {
//        return insertSelective(t);
//    }
//
//    @Override
//    public <T> int insertBatch(List<T> list) throws Exception {
//        Map<String, Object> param = new HashMap<String, Object>();
//
//        String tableName = "";
//        List<String> columns = new ArrayList<String>();
//
//        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
//
//        for (T t : list) {
//            if (tableName.equals("")) {
//                Class<?> clazz = t.getClass();
//                tableName = GeneralMapperReflectUtil.getTableName(clazz);
//            }
//            if (columns.size() == 0) {
//                Class<?> clazz = t.getClass();
//                columns = GeneralMapperReflectUtil.getAllColumns(clazz);
//            }
//            Map<String, String> mapping = GeneralMapperReflectUtil.getAllFieldValueMapping(t);
//            dataList.add(mapping);
//        }
//
//        param.put("tableName", tableName);
//        param.put("columns", columns);
//        param.put("dataList", dataList);
//
//        return dao.insertBatch(param);
//    }
//
//    @Override
//    public <T> int deleteByPrimaryKey(Class<T> clazz, String primaryValue) {
//        Map<String, Object> param = new HashMap<String, Object>();
//
//        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
//        String primaryKey = GeneralMapperReflectUtil.getPrimaryKey(clazz);
//
//        param.put("tableName", tableName);
//        param.put("primaryKey", primaryKey);
//        param.put("primaryValue", primaryValue);
//
//        return dao.deleteByPrimaryKey(param);
//    }
//
//    @Override
//    public <T> int deleteByCondition(Class<T> clazz, String conditionExp, Map<String, Object> conditionParam) {
//        Map<String, Object> param = new HashMap<String, Object>();
//
//        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
//
//        param.put("tableName", tableName);
//        param.put("conditionExp", conditionExp);
//        param.put("conditionParam", conditionParam);
//
//        return dao.deleteByCondition(param);
//    }
//
//    @Override
//    public <T> int updateByPrimaryKey(T t) throws Exception {
//        Map<String, Object> param = new HashMap<String, Object>();
//
//        Class<?> clazz = t.getClass();
//
//        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
//        String primaryKey = GeneralMapperReflectUtil.getPrimaryKey(clazz);
//
//        Map<String, String> mapping = GeneralMapperReflectUtil.getAllFieldValueMapping(t);
//
//        String primaryValue = mapping.get(primaryKey);
//
//        mapping.remove(primaryKey);
//
//        param.put("tableName", tableName);
//        param.put("primaryKey", primaryKey);
//        param.put("primaryValue", primaryValue);
//        param.put("columnValueMapping", mapping);
//
//        return dao.updateByPrimaryKey(param);
//    }
//
//    @Override
//    public <T> int updateByPrimaryKeySelective(T t) throws Exception {
//        Map<String, Object> param = new HashMap<String, Object>();
//
//        Class<?> clazz = t.getClass();
//
//        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
//        String primaryKey = GeneralMapperReflectUtil.getPrimaryKey(clazz);
//
//        Map<String, String> mapping = GeneralMapperReflectUtil.getFieldValueMappingExceptNull(t);
//
//        String primaryValue = mapping.get(primaryKey);
//
//        mapping.remove(primaryKey);
//
//        param.put("tableName", tableName);
//        param.put("primaryKey", primaryKey);
//        param.put("primaryValue", primaryValue);
//        param.put("columnValueMapping", mapping);
//
//        return dao.updateByPrimaryKey(param);
//    }
//
//    @Override
//    public <T> int updateByConditionSelective(Class<T> clazz, Map<String, Object> columnValueMapping,
//                                              String conditionExp, Map<String, Object> conditionParam) throws Exception {
//        Map<String, Object> param = new HashMap<String, Object>();
//
//        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
//
//        param.put("tableName", tableName);
//        param.put("columnValueMapping", columnValueMapping);
//        param.put("conditionExp", conditionExp);
//        param.put("conditionParam", conditionParam);
//
//        return dao.updateByConditionSelective(param);
//    }
//
//    @Deprecated
//    public <T> List<T> selectByCondition(Class<T> clazz, String conditionExp, Map<String, Object> conditionParam)
//            throws Exception {
//        Map<String, Object> param = new HashMap<String, Object>();
//
//        List<String> queryColumn = GeneralMapperReflectUtil.getAllColumns(clazz);
//
//        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
//
//        param.put("queryColumn", queryColumn);
//        param.put("tableName", tableName);
//        param.put("conditionExp", conditionExp);
//        param.put("conditionParam", conditionParam);
//
//        List<Map<String, Object>> list = dao.selectAdvanced(param);
//        List<T> result = new ArrayList<T>();
//        for (Map<String, Object> mapping : list) {
//            result.add(GeneralMapperReflectUtil.parseToBean(mapping, clazz));
//        }
//
//        return result;
//    }
//
//    @Override
//    public <T> List<T> selectAdvanced(Class<T> clazz, CrudParam queryParam) throws Exception {
//        List<T> result = new ArrayList<>();
//
//        queryParam.setQueryColumn(GeneralMapperReflectUtil.getAllColumns(clazz));
//
//        List<Map<String, Object>> list = selectAdvancedByColumn(clazz, queryParam);
//
//        if (list != null && list.size() != 0) {
//            for (Map<String, Object> mapping : list) {
//                result.add(GeneralMapperReflectUtil.parseToBean(mapping, clazz));
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public <T> List<Map<String, Object>> selectAdvancedByColumn(Class<T> clazz, CrudParam queryParam)
//            throws Exception {
//        Map<String, Object> param = new HashMap<String, Object>();
//
//        String tableName = GeneralMapperReflectUtil.getTableName(clazz);
//
//        param.put("tableName", tableName);
//        param.put("queryColumn", queryParam.getQueryColumn());
//        param.put("conditionExp", queryParam.getConditionExp());
//        param.put("conditionParam", queryParam.getConditionParam());
//        param.put("orderExp", queryParam.getOrderExp());
//
//        if (queryParam.getPageSize() != null && queryParam.getPageNo() != null) {
//            Map<String, Integer> page = new HashMap<String, Integer>();
//            page.put("pageSize", queryParam.getPageSize());
//            page.put("startRowNo", (queryParam.getPageNo() - 1) * queryParam.getPageSize());
//            param.put("page", page);
//        }
//
//        return dao.selectAdvanced(param);
//    }
//

}
