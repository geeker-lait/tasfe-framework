package com.tasfe.framework.crud.mysql.rws;

/**
 * Created by Lait on 2017/8/6.
 */
public final class DynamicDataSourceHolder {
    private static final ThreadLocal<DynamicDataSourceType> holder = new ThreadLocal<DynamicDataSourceType>();

    private DynamicDataSourceHolder() {
    }

    public static void putDataSource(DynamicDataSourceType dataSource) {
        holder.set(dataSource);
    }

    public static DynamicDataSourceType getDataSource() {
        return holder.get();
    }

    public static void clearDataSource() {
        holder.remove();
    }

}
