package com.tasfe.framework.logagent.logback.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by lait on 2016/12/16.
 */
public class PropertyUtils {
    private static Properties properties = null;

    public static Properties getProperties() {
        if (properties == null)
            //properties = PropertyPlaceholderConfigurer.getPro();
        if (properties == null || properties.isEmpty()) {
            throw new RuntimeException("no configrations key by properties or zookeeper!");
        }
        return properties;
    }

}
