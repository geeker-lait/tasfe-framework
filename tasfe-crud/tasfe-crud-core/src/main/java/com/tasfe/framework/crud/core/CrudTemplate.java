package com.tasfe.framework.crud.core;

import com.tasfe.framework.crud.api.CrudMethod;
import com.tasfe.framework.crud.api.CrudOperator;
import com.tasfe.framework.crud.api.Crudable;
import com.tasfe.framework.crud.api.StoragerType;
import com.tasfe.framework.crud.api.annotation.Route;
import com.tasfe.framework.crud.api.annotation.Storager;
import com.tasfe.framework.crud.api.dto.CrudParam;
import com.tasfe.framework.crud.api.dto.Pagination;
import com.tasfe.framework.crud.api.dto.QueryParam;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 利用mybatis的sqlSession,实现默认方式
 * Created by Lait on 2017/5/29.
 */
public abstract class CrudTemplate implements CrudOperator,ApplicationContextAware {

    private static int DEFAULT_OFFSET = 1;
    private static int DEFAULT_LIMIT = 5;

    private static ThreadLocal<String> method = new ThreadLocal<>();
    private static ThreadLocal<Object> param = new ThreadLocal<>();

    private ApplicationContext applicationContext;
    @Autowired
    private CrudRouter crudRouter;


    /**
     * 自定义xml文件，设置参数
     * @return Class
     */
    public CrudTemplate forParam(Object param) {
        CrudTemplate.param.set(param);
        return this;
    }

    /**
     * 自定义xml文件,设置执行方法名
     * @param method
     * @return
     */
    public CrudTemplate exec(String method) {
        CrudTemplate.method.set(method);
        return this;
    }

    /**
     * 自定义xml文件,执行并填充对象模型
     * @param clazz
     * @param <Entity>
     * @return
     */
    public <Entity> Entity  fill(Class<Entity> clazz) {
        Object param = CrudTemplate.param.get();
        String method = CrudTemplate.method.get();
        Entity entity = null;
        //Entity entity = this.getSqlSession().selectOne(getStatementName(method), param);
        CrudTemplate.param.remove();
        CrudTemplate.method.remove();
        return entity;
    }


    /**
     * 掺入数据
     *
     * @return
     */
    @Override
    public <T> void insert(T record) throws Exception {
        QueryParam queryParam = new QueryParam(CrudMethod.IN,record);
        route(queryParam);
    }

    @Override
    public <T> void insertBatch(List<T> records) throws Exception {
        QueryParam queryParam = new QueryParam(CrudMethod.INS,records);
        route(queryParam);
    }


    /**
     * 获取数据
     * @param id
     * @param <T>
     * @return
     * @throws Exception
     */
    @Override
    public <T> T get(Class<T> entiyClass,Long id) throws Exception {
        QueryParam queryParam = new QueryParam(CrudMethod.GET,entiyClass,id);
        return (T) route(queryParam);
    }

    @Override
    public <Entity> List<Entity> gets(Class<Entity> entiyClass,Long... ids) throws Exception {
        QueryParam queryParam = new QueryParam(CrudMethod.GETS,entiyClass,ids);
        return (List<Entity>) route(queryParam);
    }

    @Override
    public <Entity> List<Entity> gets(Entity entity) throws Exception {
        QueryParam queryParam = new QueryParam(CrudMethod.GETS,entity);
        return (List<Entity>) route(queryParam);
    }

    @Override
    public <Entity> List<Entity> find(Entity entity) {


        return null;
    }


    /**
     * 删除数据
     * @param id
     */
    @Override
    public void del(Long id) {

    }
    @Override
    public void dels(Long... ids) {

    }
    @Override
    public <T> void del(T records) {

    }


    /**
     * 更新数据
     * @param entity
     * @param <Entity>
     */
    @Override
    public <Entity> void update(Entity entity) {

    }
    @Override
    public <Entity> void updates(List<Entity> entities) {

    }


    /**
     * 分页数据
     * @param pager
     * @param example 查询条件参数
     * @param <Entity>
     * @return
     */
    @Override
    public <Entity> List<Entity> pagination(Pagination pager, Entity example) {
        return null;
    }

    @Override
    public <Entity> Long counts(Entity entity) {
        return null;
    }


    /**
     * 路由数据
     * @param queryParam
     * @return
     * @throws Exception
     * CrudMethod cm,Class<T> clazz,T entity,List<T> tlist,QueryParam param
     */
    @Route
    private Object route(QueryParam queryParam) throws Exception {
        Class<?> clazz = queryParam.getQueryClazz();
        Storager storager = clazz.getAnnotation(Storager.class);
        if (storager != null) {
            StoragerType[] storagerTypes = storager.storage();
            for (StoragerType storagerType : storagerTypes) {
                Crudable crudable = applicationContext.getBean(Crudable.class, storagerType);
                CrudMethod cm = CrudMethod.get(queryParam.getCrudMethod());
                switch (cm) {
                    case IN:
                        crudable._in(queryParam.getEntity());
                        break;
                    case INS:
                        crudable._ins(queryParam.getEntityList());
                        break;
                    case GET:
                        return crudable._get(clazz,queryParam.getPk());
                    case GETS:
                        return crudable._gets(clazz,queryParam);
                    case LIST:
                        break;
                    case FIND:
                        break;
                    case UPD:
                        //crudable.upd(t);
                        break;
                    case UPDS:
                        //crudable.upds(tlist);
                        break;
                    case DEL:
                        break;
                    case DELS:
                        break;


                }
            }
        }
        return null;

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }




    /* public Object ins() {
        Object param = CrudTemplate.param.get();
        String method = CrudTemplate.method.get();
        Object o = this.getSqlSession().insert(getStatementName(method), param);
        CrudTemplate.param.remove();
        CrudTemplate.method.remove();
        return o;
    }*/


    /**
     * 更新数据
     *
     * @return
     */
    /*public Object upd() {
        Object param = CrudTemplate.param.get();
        String method = CrudTemplate.method.get();
        Object o = this.getSqlSession().update(getStatementName(method), param);
        CrudTemplate.param.remove();
        CrudTemplate.method.remove();
        return o;
    }*/


    /**
     * 删除数据
     *
     * @return
     */
    /*public Object del() {
        Object param = CrudTemplate.param.get();
        String method = CrudTemplate.method.get();
        Object o =  this.getSqlSession().delete(getStatementName(method), param);
        CrudTemplate.param.remove();
        CrudTemplate.method.remove();
        return o;
    }*/




    /*public <Entity> List<Entity> list() {
        Object param = CrudTemplate.param.get();
        String method = CrudTemplate.method.get();
        List<Entity> entityList = this.getSqlSession().selectList(getStatementName(method), param);
        CrudTemplate.param.remove();
        CrudTemplate.method.remove();
        return entityList;
    }*/


    /**
     * 返回命名空间的值
     *
     * @return String
     */
    /*private String getSqlNamespace() {
        Object param = CrudTemplate.param.get();
        String className = param.getClass().getName();
        return className;
    }*/

    /**
     * 获取需要操作sql的id，forEnity().getClass().getName()无法满足时，可以重载此方法
     *
     * @param statementName SQL的ID(不包含namespace)
     * @return String
     */
    /*protected String getStatementName(String statementName) {
        return *//*getSqlNamespace() + "." + *//*statementName;
    }*/


    /*private Long count(Map<String, Object> m) {
        String method = CrudTemplate.method.get();
        Long count = this.getSqlSession().selectOne(getStatementName(method), m);
        CrudTemplate.method.remove();
        return count;
    }*/

    /**
     * 分页查询
     *
     * @param countPageEach 可点击页码个数
     * @param param             表单查询参数
     * @return Page对象
     */
    /*public <Entity> Pagination<Entity> paging(int countPageEach, QueryParam param) {
        Map m = param.getConditionParam();
        int curPage = !m.containsKey("offset") ? DEFAULT_OFFSET : Integer.parseInt(m.get("offset").toString());
        int limit = !m.containsKey("limit") ? DEFAULT_LIMIT : Integer.parseInt(m.get("limit").toString());
        int offset = curPage * limit - (limit - 1);
        //总记录数
        long totalCount = count(m);
        //总页码
        long countPage = totalCount % limit > 0 ? totalCount / limit + 1 : totalCount / limit;
        //mysql index 从0开始，所以要减一；oracle index 从1开始
        offset--;

        m.put("offset", String.valueOf(offset));
        m.put("limit", String.valueOf(limit));
        String method = CrudTemplate.method.get();
        List<Entity> result = this.getSqlSession().selectList(getStatementName(method), m);
        return new Pagination<Entity>(curPage, countPage, countPageEach, limit, result);
    }*/


}
