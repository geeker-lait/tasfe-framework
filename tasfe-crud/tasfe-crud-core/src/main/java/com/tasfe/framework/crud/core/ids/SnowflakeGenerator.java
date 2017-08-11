package com.tasfe.framework.crud.core.ids;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * Created by Lait on 2017/8/6.
 */
public class SnowflakeGenerator implements IdentifierGenerator {
    public static final String TYPE = "com.tasfe.framework.crud.core.ids.SnowflakeGenerator";

    private static final IdWorker idWorker = new IdWorker();

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return idWorker.getId();
    }

    /* @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        return idWorker.getId();
    }*/
}
