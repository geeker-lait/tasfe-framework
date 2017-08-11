package com.tasfe.framework.crud.core.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 对象转换器.
 * S source
 * D destination
 * ID source id
 * Created by Lait on 2017/8/6.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class AbstractConverter<S, D, ID> implements ConverterEx<S, D, ID>, InitializingBean {

    protected Logger LOG = LoggerFactory.getLogger(getClass());

    private Map<String, Object> hints = Collections.emptyMap();

    protected List<ConverterAssembler> assemblers = new ArrayList<ConverterAssembler>();

    public static <T> List<T> trimNull(List<T> list) {
        List<T> list1 = new ArrayList<T>(list.size());
        for (T item : list) {
            if (item != null) {
                list1.add(item);
            }
        }
        return list1;
    }

    protected void initAssemblers(List<ConverterAssembler> assemblers) {
        //implement it
    }

    public void setHints(Map<String, Object> hints) {
        this.hints = hints;
    }

    protected void setAssemblers(List<ConverterAssembler> assemblers) {
        this.assemblers = assemblers;
    }


    @Override
    public D convert(S source) {
        if (source == null) {
            return null;
        }
        D d = internalConvert(source);
        if (d != null) {
            for (ConverterAssembler assembler : assemblers) {
                if (assembler.accept(hints)) {
                    Set keys = assembler.getKeys(source, d);
                    if (keys != null) {
                        Map values = new HashMap();
                        if (keys.size() == 1) {
                            Object key = keys.iterator().next();
                            if (key != null) {
                                values.put(key, assembler.getValue(key));
                            }
                        } else {
                            values = assembler.mgetValue(trimNull(keys));
                        }
                        assembler.setValues(source, d, values);
                    }
                }
            }

        }
        return d;
    }

    private Set trimNull(Set keys) {
        Set set = new HashSet();
        for (Object o : keys) {
            if (o != null) {
                set.add(o);
            }
        }
        return set;
    }

    @Override
    public List<D> convert(List<S> source) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        List<D> ds = new ArrayList<D>(source.size());
        for (S s : source) {
            ds.add(convert(s));
        }
        assembler(source, ds);
        return ds;
    }

    @Override
    public Map<ID, D> convert(Map<ID, S> source) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyMap();
        }
        final int len = source.size();
        List<S> ss = new ArrayList<S>(source.values());
        List<D> ds = convert(ss);
        Map<ID, D> dMap = new HashMap<ID, D>(ss.size());
        for (int i = 0; i < len; i++) {
            dMap.put(getId(ss.get(i)), ds.get(i));
        }
        return dMap;
    }

    @Override
    public Page<D> convert(Page<S> source) {
        return new PageImpl<D>(convert(source.getContent()), source.previousPageable(), source.getTotalElements());
    }

    @Override
    public D get(ID id) {
        return convert(internalGet(id));
    }

    @Override
    public Map<ID, D> mget(Collection<ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return convert(internalMGet(ids));
    }

    @Override
    public List<ID> getIds(List<S> source) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        List<ID> ids = new ArrayList<ID>(source.size());
        for (S s : source) {
            ids.add(getId(s));
        }
        return ids;
    }

    private void assembler(List<S> ss, List<D> ds) {
        int len = ss.size();
        List<Object> keys = new ArrayList<Object>(len);
        for (ConverterAssembler assembler : assemblers) {
            if (assembler.accept(hints)) {
                keys.clear();
                for (int i = 0; i < len; i++) {
                    Set ks = assembler.getKeys(ss.get(i), ds.get(i));
                    if (ks != null) {
                        keys.addAll(ks);
                    }
                }
                List<Object> realKeys = trimNull(keys);
                if (!CollectionUtils.isEmpty(realKeys)) {
                    Map<Object, Object> map = assembler.mgetValue(realKeys);
                    if (!CollectionUtils.isEmpty(map)) {
                        for (int i = 0; i < ds.size(); i++) {
                            assembler.setValues(ss.get(i), ds.get(i), map);
                        }
                    }
                }
            }
        }
    }


    protected abstract ID getId(S source);

    protected abstract D internalConvert(S source);

    protected abstract S internalGet(ID id);

    protected abstract Map<ID, S> internalMGet(Collection<ID> ids);


    @Override
    public void afterPropertiesSet() throws Exception {
        initAssemblers(assemblers);
    }
}
