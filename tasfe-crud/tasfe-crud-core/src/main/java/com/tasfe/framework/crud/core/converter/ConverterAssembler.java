package com.tasfe.framework.crud.core.converter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 对象转换组装器
 *
 * @param <S> 源对象
 * @param <D> 目标对象
 * @param <K> 获取值的key
 * @param <V> 值
 *            <p>
 * Created by Lait on 2017/8/6.
 */
public interface ConverterAssembler<S, D, K, V> {

    /**
     * 根据传入的提示判断是否需要装配
     *
     * @param hints 提示条件
     * @return 是否装配
     */
    boolean accept(Map<String, Object> hints);

    /**
     * 从原对象中提取key
     *
     * @param s 源对象
     * @param d 目标对象
     * @return 键
     */
    Set<K> getKeys(S s, D d);

    /**
     * 设置源对象值
     *
     * @param s      源对象
     * @param d      目标对象
     * @param values 值
     */
    void setValues(S s, D d, Map<K, V> values);

    /**
     * 根据键获取目标值
     *
     * @param key 键
     * @return value
     */
    V getValue(K key);

    /**
     * 批量获取值
     *
     * @param keys 键列表
     * @return 值map
     */
    Map<K, V> mgetValue(Collection<K> keys);
}
