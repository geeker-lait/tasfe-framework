package com.tasfe.framework.netty.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author liat.zhang@gmail.com
 * @date 2016年9月16日-下午10:15:47
 * @description <code>ConfigurationReader</code>类是配置项读取器
 * <p>
 * 功能详细描述：系统加载时，本读取器将加载<b>server.properties</b>配置文件并读取所有配置项缓存到系统内存中。
 * 本类为单例实例，获取配置项示例代码如下：
 * <pre>
 *              ServerConfigReader configReader = ServerConfigReader.getInstance();
 *              String appServerUrl = configReader.getString(&quot;app.server.port&quot;);
 *              </pre>
 */
public final class ServerConfigReader {
    /**
     * 默认读取配置文件的路径
     */
    public static final String TASFE_SERVER_PROPERTIES = "server.properties";
    /**
     * 系统配置文件读取器
     */
    private static ServerConfigReader configReader = new ServerConfigReader();
    /** 客户端配置路径 */
    //private final static String BASE_NAME = "server";
    /**
     * 配置属性集合
     */
    //private ResourceBundle resourceBundle = null;
    private Properties properties = new Properties();

    private String serverProperties = TASFE_SERVER_PROPERTIES;

    /**
     * 私有构造器
     */
    private ServerConfigReader() {
    }


    public ServerConfigReader init(String props) {
        if (props != null) {
            serverProperties = props;
        }
        try {
            Resource res = new ClassPathResource(serverProperties);
            properties.load(res.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load server.properties!");
        }
        /**
         * 如果本地没有配置走配置中心
         */
        if (properties.isEmpty()) {
            //properties = PropertyPlaceholderConfigurer.getPro();
        }
        return this;
    }

    /**
     * 获取配置文件读取器对象
     *
     * @return 配置文件读取器对象
     */
    public static ServerConfigReader getInstance() {
        return configReader;
    }


    /**
     * 根据key获取值
     * <p>
     * 值类型为字符串
     *
     * @param key 配置键
     * @return 字符类型的值
     */
    public String getString(String key) {
        if (null != key && "".equals(key)) {
            return null;
        }
        try {
            return properties.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据key获取值
     * <p>
     * 值类型为Int数值类型
     *
     * @param key 配置键
     * @return Int数值类型的值
     */
    public Integer getInt(String key) {
        if (null != key && "".equals(key)) {
            return null;
        }
        String value = this.getString(key);
        if (null == value) {
            value = "0";
        }
        return Integer.valueOf(value);
    }

    /**
     * 根据key获取值
     * <p>
     * 值类型为Boolean类型
     *
     * @param key 配置键
     * @return Boolean类型的值
     */
    public Boolean getBoolean(String key) {
        if (null != key && "".equals(key)) {
            return null;
        }
        return Boolean.valueOf(this.getString(key));
    }
}
