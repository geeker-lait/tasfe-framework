package com.tasfe.framework.crud.core;

import com.tasfe.framework.crud.api.CrudMethod;
import com.tasfe.framework.crud.api.CrudOperator;
import com.tasfe.framework.crud.api.dto.Pagination;
import com.tasfe.framework.crud.api.dto.QueryParam;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * 利用mybatis的sqlSession,实现默认方式
 * Created by Lait on 2017/5/29.
 */
public abstract class CrudTemplate implements CrudOperator,ApplicationContextAware {

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
        crudRouter.route(queryParam);
    }

    @Override
    public <T> void insertBatch(List<T> records) throws Exception {
        QueryParam queryParam = new QueryParam(CrudMethod.INS,records);
        crudRouter.route(queryParam);
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
        return (T) crudRouter.route(queryParam);
    }

    @Override
    public <Entity> List<Entity> gets(Class<Entity> entiyClass,Long... ids) throws Exception {
        QueryParam queryParam = new QueryParam(CrudMethod.GETS,entiyClass,ids);
        return (List<Entity>) crudRouter.route(queryParam);
    }

    @Override
    public <Entity> List<Entity> gets(Entity entity) throws Exception {
        QueryParam queryParam = new QueryParam(CrudMethod.GETS,entity);
        return (List<Entity>) crudRouter.route(queryParam);
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


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
