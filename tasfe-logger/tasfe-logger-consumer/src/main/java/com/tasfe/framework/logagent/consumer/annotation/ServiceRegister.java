package com.tasfe.framework.logagent.consumer.annotation;

import com.tasfe.framework.logagent.consumer.api.IStorageApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegister {
    private static Logger logger = LoggerFactory.getLogger(ServiceRegister.class);
    private static ServiceRegister instance = null;
    private static ConcurrentHashMap<String, IStorageApi> services  = new ConcurrentHashMap<>();


    private ServiceRegister() {
    }

    public static synchronized ServiceRegister getInstance() {
        if (instance == null) {
            instance = new ServiceRegister();
        }
        return instance;
    }

    public void register(IStorageApi storageApi) {
        Provider provider = storageApi.getClass().getAnnotation(Provider.class);
        String code = provider.value();
        if (!services.containsKey(code)) {
            services.put(code, storageApi);
        }
    }

    public IStorageApi getProvider(String code) {
        if (!services.containsKey(code)) {
            logger.error("provider %s not exsist", code);
        }
        return services.get(code);
    }

}
