package com.tasfe.framework.crud.api.params;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.CrudMethod;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by Lait on 2017/4/15
 * sql查询参数
 */
@Data
public class CrudParam {
    /**
     * 单主键
     */
    private Long pk;

    /**
     * 多主键
     */
    private Long[] pks;

    /**
     * 表对应的pojo类
     */
    private Class<?> queryClazz;

    /**
     * 查询列
     */
    private List<String> queryColumn;

    /**
     * 查询条件 where 表达式
     * <p>不要写where</p>
     * <p>传入的参数格式为:#{conditionParam.paramName}</p>
     */
    private String conditionExp;


    /**
     *  特殊的自定义的查询条件
     */
    private Criteria criteria;

    /**
     * 表达式
     */
    //private List<Kvc> kvcs;

    /**
     * 查询条件 where 表达式中的参数集
     * <p>key:paramName</p>
     */
    private Map<String, Object> conditionParam;

    /**
     * 每页的数据条数
     * <p>分页查询使用</p>
     */
    private Integer pageSize;

    /**
     * 页码
     * <p>分页查询使用</p>
     */
    private Integer pageNo;

    /**
     * order by 表达式
     * <p>格式:columnName asc|desc</p>
     * <p>多个排序字段用逗号分隔</p>
     */
    private String orderExp;


    /**
     * crud method
     */
    private String crudMethod;

    /**
     * crud实体参数
     */
    private Object entity;

    /**
     * crud实体集合参数
     */
    private List<?> entityList;




    public CrudParam() {
    }


    public <T> CrudParam(CrudMethod cm, Criteria criteria) {
        this.crudMethod = cm.toString();
        this.criteria = criteria;
        this.queryClazz = criteria.getClazz();
    }

    public <T> CrudParam(CrudMethod cm, List<T> records) {
        this.crudMethod = cm.toString();
        this.entityList = records;
    }

    public <T> CrudParam(CrudMethod cm, T record) {
        this.crudMethod = cm.toString();
        this.entity = record;
    }

    public <T> CrudParam(CrudMethod cm, Class<T> entiyClass, Long id) {
        this.crudMethod = cm.toString();
        this.queryClazz = entiyClass;
        this.pk = id;
    }

    public <T> CrudParam(CrudMethod cm, Class<T> entiyClass, Long[] ids) {
        this.crudMethod = cm.toString();
        this.queryClazz = entiyClass;
        this.pks = ids;
    }


    public Class<?> getQueryClazz() {
        if(queryClazz != null) {
            return queryClazz;
        } else if(entity != null){
            return this.entity.getClass();
        } else if(entityList != null){
            return entityList.get(0).getClass();
        }
        return null;
    }

/*    public void setQueryClazz(Class<?> queryClazz) {
        this.queryClazz = queryClazz;
    }

    public List<String> getQueryColumn() {
        return queryColumn;
    }

    public void setQueryColumn(List<String> queryColumn) {
        this.queryColumn = queryColumn;
    }

    public String getConditionExp() {
        return conditionExp;
    }

    public void setConditionExp(String conditionExp) {
        this.conditionExp = conditionExp;
    }

    public Map<String, Object> getConditionParam() {
        return conditionParam;
    }

    public void setConditionParam(Map<String, Object> conditionParam) {
        this.conditionParam = conditionParam;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getOrderExp() {
        return orderExp;
    }

    public void setOrderExp(String orderExp) {
        this.orderExp = orderExp;
    }

    public String getCrudMethod() {
        return crudMethod;
    }

    public void setCrudMethod(String crudMethod) {
        this.crudMethod = crudMethod;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public List<?> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<?> entityList) {
        this.entityList = entityList;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public Long[] getPks() {
        return pks;
    }

    public void setPks(Long[] pks) {
        this.pks = pks;
    }*/
}
