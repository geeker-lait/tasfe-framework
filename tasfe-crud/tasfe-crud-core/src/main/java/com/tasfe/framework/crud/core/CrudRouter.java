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

import java.lang.reflect.Field;


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
        if(clazz !=null ){
            Storager storager = clazz.getAnnotation(Storager.class);
            Crudable crudable = null;
            StoragerType[] storagerTypes = null;
            if (storager != null) {
                storagerTypes = storager.storage();
            } else {
                storagerTypes = new StoragerType[]{StoragerType.MYSQL};
            }

            for (StoragerType storagerType : storagerTypes) {
                crudable = (Crudable)applicationContext.getBean(storagerType.toString().toLowerCase());
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
                    case COUNT:
                        return crudable._count(clazz,queryParam);
                    case LIST:
                        break;
                    case FIND:
                        break;
                    case UPD:
                        return crudable._upd(queryParam.getEntity());
                    case UPDS:
                        //crudable.upds(tlist);
                        break;
                    case DEL:
                        crudable._del(queryParam);
                        break;
                    case DELS:
                        break;
                }
            }
        } else {
            // 做点提示
        }

        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
