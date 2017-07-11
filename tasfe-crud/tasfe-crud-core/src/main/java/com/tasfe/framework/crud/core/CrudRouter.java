package com.tasfe.framework.crud.core;

import com.tasfe.framework.crud.api.CrudMethod;
import com.tasfe.framework.crud.api.Crudable;
import com.tasfe.framework.crud.api.StoragerType;
import com.tasfe.framework.crud.api.annotation.Route;
import com.tasfe.framework.crud.api.annotation.Storager;
import com.tasfe.framework.crud.api.dto.QueryParam;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Created by Lait on 2017/5/29.
 */
@Service
public class CrudRouter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 路由数据
     * @param queryParam
     * @return
     * @throws Exception
     * CrudMethod cm,Class<T> clazz,T entity,List<T> tlist,QueryParam param
     */
    @Route
    public Object route(QueryParam queryParam) throws Exception {
        Class<?> clazz = queryParam.getQueryClazz();
        Storager storager = clazz.getAnnotation(Storager.class);
        if (storager != null) {
            StoragerType[] storagerTypes = storager.storage();
            for (StoragerType storagerType : storagerTypes) {
                Crudable crudable = (Crudable)applicationContext.getBean(storagerType.toString());
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
}
