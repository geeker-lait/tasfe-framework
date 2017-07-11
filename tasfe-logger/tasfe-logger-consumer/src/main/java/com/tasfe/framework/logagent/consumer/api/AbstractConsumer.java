package com.tasfe.framework.logagent.consumer.api;

import com.tasfe.framework.logagent.consumer.annotation.ServiceRegister;

/**
 * Created by lait on 2017/6/21.
 */
public abstract class AbstractConsumer implements IStorageApi{
    public AbstractConsumer() {
        ServiceRegister.getInstance().register(this);
    }
}
