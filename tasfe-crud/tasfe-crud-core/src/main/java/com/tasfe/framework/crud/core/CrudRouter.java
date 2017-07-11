package com.tasfe.framework.crud.core;

import com.tasfe.framework.crud.api.CrudMethod;
import com.tasfe.framework.crud.api.Crudable;
import com.tasfe.framework.crud.api.StoragerType;
import com.tasfe.framework.crud.api.annotation.Route;
import com.tasfe.framework.crud.api.annotation.Storager;
import com.tasfe.framework.crud.api.dto.CrudParam;
import com.tasfe.framework.crud.api.dto.QueryParam;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Lait on 2017/5/29.
 */
@Service
public class CrudRouter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Route
    public <T> T route(CrudParam crudParam) throws Exception {
        T t = null;
        List<T> ts = null;
        /*if(obj instanceof List){
            Method m = CrudRouter.class.getMethod("route", CrudMethod.class,Object.class);
            Type [] ts = m.getGenericParameterTypes();//获取参数泛型
            for(Type paramType:ts){
                System.out.println("#"+paramType);
                if(paramType instanceof ParameterizedType){
                    Type[]genericTypes = ((ParameterizedType)paramType).getActualTypeArguments();
                    for(Type genericType:genericTypes){
                        System.out.println("泛型类型"+genericType);
                    }
                }
            }
            Type type = obj.getClass().getGenericSuperclass();
            //获取泛型参数的实际类型
            Class clazz=(Class<T>)((ParameterizedType)type).getActualTypeArguments()[0];
        }*/


        //Method declaredMethod = ReflectionUtils.findMethod(CrudRouter.class,"route");
        Method declaredMethod = CrudRouter.class.getDeclaredMethod("route",QueryParam.class);
        //获取到方法的参数列表
        Type[] parameterTypes = declaredMethod.getGenericParameterTypes();
        for (Type type : parameterTypes) {
            System.out.println(type);
            //只有带泛型的参数才是这种Type，所以得判断一下
            if(type instanceof ParameterizedType){
                ParameterizedType parameterizedType = (ParameterizedType) type;
                //获取参数的类型
                System.out.println(parameterizedType.getRawType());
                //获取参数的泛型列表
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for (Type type2 : actualTypeArguments) {
                    System.out.println(type2);
                }
            }
        }

        //Type mySuperClass = entitys.getClass().getGenericSuperclass();
        //Type type = ((ParameterizedType)mySuperClass).getActualTypeArguments()[0];

        /*Storager storager = type.getClass().getAnnotation(Storager.class);
        if (storager != null) {
            StoragerType[] storagerTypes = storager.storage();
            for (StoragerType storagerType : storagerTypes) {
                Crudable crudable = applicationContext.getBean(Crudable.class, storagerType);
                switch (in) {
                    case IN:
                        crudable.in(entity);
                        break;
                    case INS:
                        crudable.ins(entitys);
                        break;

                    case GET:
                        crudable.get(clazz, pk);
                        break;
                    case UPD:
                        //crudable.upd(t);
                        break;
                    case UPDS:
                        //crudable.upds(t);
                        break;

                }
            }
        }*/
        return null;

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
