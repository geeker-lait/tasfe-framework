package com.tasfe.framework.crud.core.utils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Created by Lait on 2017/7/7.
 */
public class ClassReflectUtil {
    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
     * 如无法找到, 返回Object.class.
     * eg.
     * public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    public static Class getClassGenricType(final Class clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    public static Class getClass(Type type, int i) {
        // 处理泛型类型
        if (type instanceof ParameterizedType) {
            System.out.println("1111111");
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            System.out.println("--------" + ((Class) getClass(((TypeVariable) type).getBounds()[0], 0)).getName());
            // 处理泛型擦拭对象
            return (Class) getClass(((TypeVariable) type).getBounds()[0], 0);
        } else {
            // class本身也是type，强制转型
            return (Class) type;
        }
    }

    public static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) {
            // 处理多级泛型
            System.out.println("111111");
            return (Class) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) {
            // 处理数组泛型
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) {
            // 处理泛型擦拭对象
            System.out.println("33333333");
            return (Class) getClass(((TypeVariable) genericClass).getBounds()[0], 0);
        } else {
            System.out.println("444444");
            return (Class) genericClass;
        }
    }
}
