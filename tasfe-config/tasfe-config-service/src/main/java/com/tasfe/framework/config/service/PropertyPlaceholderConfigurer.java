package com.tasfe.framework.config.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 自定义的配置处理加密properties
 * Created by Lait on 2017/7/11.
 */
public class PropertyPlaceholderConfigurer extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyPlaceholderConfigurer.class);
    private static Properties properties = new Properties();
    private static final String ENCRYPT_KEY_NAME = "ca9531ba0d3b4546a89b5de01065cbed3379b02675a049df961ef47676d20183";

    public PropertyPlaceholderConfigurer() {
    }

    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        Map staticMap = null;

        try {
            String encryptKey = props.getProperty(ENCRYPT_KEY_NAME);
            Properties properties = new Properties();
            Iterator i$ = props.keySet().iterator();

            String secretKey;
            String secretValue;
            while (i$.hasNext()) {
                Object obj = i$.next();
                if (!ENCRYPT_KEY_NAME.equals(obj)) {
                    secretKey = EncryptUtils.decryptMode((String) obj, encryptKey);
                    secretValue = EncryptUtils.decryptMode(props.getProperty((String) obj), encryptKey);
                    properties.put(secretKey, secretValue);
                }
            }

            props.clear();
            staticMap = (new InnitCache()).innitParam(properties);
            properties.clear();
            if (staticMap != null && !staticMap.isEmpty()) {
                i$ = staticMap.entrySet().iterator();

                while (i$.hasNext()) {
                    Entry<String, Object> entry = (Entry) i$.next();
                    props.setProperty((String) entry.getKey(), entry.getValue().toString());
                    properties.setProperty((String) entry.getKey(), entry.getValue().toString());
                    secretKey = EncryptUtils.encryptMode((String) entry.getKey(), encryptKey);
                    secretValue = EncryptUtils.encryptMode(entry.getValue().toString(), encryptKey);
                    properties.setProperty(secretKey, secretValue);
                }

                properties.put(ENCRYPT_KEY_NAME, encryptKey);
                URL url = this.getClass().getClassLoader().getResource("/");
                if (url != null) {
                    String path = url.getPath();
                    if (!path.startsWith("file:")) {
                        File parentFile = (new File(path)).getParentFile();
                        if (!parentFile.isFile()) {
                            if (!parentFile.exists() || !parentFile.isDirectory()) {
                                parentFile.mkdirs();
                            }

                            File file = new File(parentFile, "conf.properties");
                            OutputStream outputStream = new FileOutputStream(file);
                            properties.store(outputStream, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
                            outputStream.close();
                        }
                    }
                }
            }
        } catch (Exception var11) {
            LOGGER.error("配置文件处理失败", var11);
            throw new RuntimeException();
        }

        if ("serverData".equals(CacheManager.getActiveMapValue("tasfeSystemActiveMapDataFrom"))) {
            Integer retryTime = Integer.valueOf(Integer.parseInt((String) staticMap.get("zk.disconnect.retryTime")));
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    PropertyPlaceholderConfigurer.LOGGER.debug("执行定时任务,从ZK获取动态参数");
                    if ("serverData".equals(CacheManager.getActiveMapValue("tasfeSystemActiveMapDataFrom"))) {
                        try {
                            if (ZkClientFactory.getZkClient() == null) {
                                throw new RuntimeException("连接ZOOKEEPER失败");
                            }

                            (new InnitCache()).innitActiveMapByZk();
                        } catch (Exception var2) {
                            PropertyPlaceholderConfigurer.LOGGER.error("通过ZK初始化动态参数失败", var2);
                        }
                    }

                    if ("zkData".equals(CacheManager.getActiveMapValue("tasfeSystemActiveMapDataFrom"))) {
                        timer.cancel();
                    }

                }
            }, (long) retryTime.intValue(), (long) retryTime.intValue());
        }

        super.processProperties(beanFactoryToProcess, props);
    }

    public static Properties getProperties() {
        return properties;
    }
}
