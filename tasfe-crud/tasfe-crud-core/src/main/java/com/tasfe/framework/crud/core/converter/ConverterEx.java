package com.tasfe.framework.crud.core.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 转换器扩展.
 * Created by Lait on 2017/8/6.
 */
public interface ConverterEx<S, D, ID> extends Converter<S, D> {

    List<D> convert(List<S> source);

    Map<ID, D> convert(Map<ID, S> source);

    Page<D> convert(Page<S> source);

    D get(ID id);

    Map<ID, D> mget(Collection<ID> ids);

    List<ID> getIds(List<S> source);
}
