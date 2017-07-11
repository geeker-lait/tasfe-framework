package com.tasfe.framework.netty.config;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liat.zhang@gmail.com
 * @date 2016年9月16日-下午10:11:11
 * @description <code>ServerConfig</code>为服务器提供全局配置信息
 */
public final class ServerConfig {

    /**
     * 日志对象
     */
    final static Logger logger = LoggerFactory.getLogger(ServerConfig.class);

    /**
     * 服务器协议 缺省为http
     */
    private static String protocol = "http";

    /**
     * 服务器端口 缺省为8080
     */
    private static int port = 8080;
    /**
     * 服务https端口
     */
    private static int httpsPort = 8443;

    /**
     * https
     */
    private static boolean ssl = false;

    /**
     * Session超时时间 默认为30分钟
     */
    private static int sessionTimeout = 30;

    /**
     * 服务器编解码集
     */
    private static String charsetName = "UTF-8";
    /**
     * 上传文件大小默认是:1024*1024 = 1048576 = 1M
     */
    private static int maxContentLength = 1024 * 1024;
    /**
     * boss线程数，也就是接受处理请求线程的数量，默认为cpu和数*2
     */
    private static int bossThreadNumber = Runtime.getRuntime().availableProcessors();
    /**
     * work线程数，处理业务线程池中线程数。缺省为CPU核数 * 2
     */
    private static int workThreadNumber = bossThreadNumber;
    /**
     * netty channel超时时间,默认时间是1分钟
     */
    private static int connetionTimeout = 1000 * 60;
    private static Charset charset;

    /**
     * key store path 路径
     */
    private static String keyStorePath;

    /**
     * keyStorePassword
     */
    private static String keyStorePassword;

    /**
     * keyPassword
     */
    private static String keyPassword;

    private static String staticResourcesFolder;

    public static String getKeyStorePath() {
        return keyStorePath;
    }

    public static String getKeyStorePassword() {
        return keyStorePassword;
    }

    public static String getKeyPassword() {
        return keyPassword;
    }

    public static void setKeyStorePath(String keyStorePath) {
        ServerConfig.keyStorePath = keyStorePath;
    }

    public static void setKeyStorePassword(String keyStorePassword) {
        ServerConfig.keyStorePassword = keyStorePassword;
    }

    public static void setKeyPassword(String keyPassword) {
        ServerConfig.keyPassword = keyPassword;
    }

    public static void setPort(int port) {
        ServerConfig.port = port;
    }

    public static int getPort() {
        return port;
    }

    public static boolean isSsl() {
        return ssl;
    }


    public static int getHttpsPort() {
        return httpsPort;
    }

    public static void setHttpsPort(int httpsPort) {
        ServerConfig.httpsPort = httpsPort;
    }

    public static void setSsl(boolean ssl) {
        ServerConfig.ssl = ssl;
    }

    public static String getProtocol() {
        return protocol;
    }

    public static int getSessionTimeout() {
        return sessionTimeout;
    }

    public static void setSessionTimeout(int sessionTimeout) {
        ServerConfig.sessionTimeout = sessionTimeout;
    }

    public static String getServerName() {
        return "Netty-Http-Server";
    }

    public static int getMaxContentLength() {
        return maxContentLength;
    }

    public static int getNioThreadNumber() {
        return bossThreadNumber;
    }

    public static int getBusinessThreadNumber() {
        return workThreadNumber;
    }

    public static int getConnectionTimeout() {
        return connetionTimeout;
    }

    public static void setBusinessThreadNumber(int businessThreadNumber) {
        ServerConfig.workThreadNumber = businessThreadNumber;
    }

    public static void setConnetionTimeout(int connetionTimeout) {
        ServerConfig.connetionTimeout = connetionTimeout;
    }

    public static void setNioThreadNumber(int nioThreadNumber) {
        ServerConfig.bossThreadNumber = nioThreadNumber;
    }

    public static void setMaxContentLength(int maxContentLength) {
        ServerConfig.maxContentLength = maxContentLength;
    }

    public static Charset getCharset() {
        charset = Charset.forName(charsetName);
        return charset;
    }

    public static String getCharsetName() {
        return charsetName;
    }

    public static void setCharsetName(String charsetName) {
        ServerConfig.charsetName = charsetName;
    }


    public static void init(String serverProperties) {
        // 加载当前服务器配置信息
        ServerConfigReader configReader = ServerConfigReader.getInstance().init(serverProperties);
        // 获取业务线程数
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        logger.info("The server cpu processors: {}", availableProcessors);
        // 接受请求线程数
        Integer tn = configReader.getInt("netty.http.server.bossThreadNumber");
        if (tn.intValue() > 0) {
            bossThreadNumber = tn.intValue();
        } else {
            bossThreadNumber *= 2;
        }
        tn = configReader.getInt("netty.http.server.workerThreadNumber");
        if (tn.intValue() > 0) {
            workThreadNumber = tn.intValue();
        } else {
            workThreadNumber *= 2;
        }

        tn = configReader.getInt("netty.http.server.connectionTimeout");
        if (tn.intValue() > 0) {
            connetionTimeout = tn.intValue();
        }

        // 获取当前服务器端口配置
        Integer currentPort = configReader.getInt("netty.http.server.port");
        if (0 < currentPort.intValue() && currentPort.intValue() < 65535) {
            port = currentPort;
        }


        // 获取上传文件大小配置
        Integer contentLength = configReader.getInt("netty.http.server.maxContentLength");
        if (null != contentLength && contentLength.intValue() > 0) {
            maxContentLength = contentLength;
        }
        // Session
        Object val = configReader.getString("netty.http.server.sessionTimeout");
        if (null != val) {
            try {
                int sessionTimeout = Integer.valueOf(val.toString());
                ServerConfig.setSessionTimeout(sessionTimeout);
            } catch (Exception e) {
                logger.error("Parse port fail!", e);
            }
        }

        // 获取当前服务器编解码集
        val = configReader.getString("netty.http.server.charset.name");
        if (null != val && !val.toString().isEmpty()) {
            charsetName = val.toString();
        }

        // 获取SSL配置
        ssl = configReader.getBoolean("netty.https.server.ssl");
        tn = configReader.getInt("netty.https.server.port");
        if (tn != null && tn > 0) {
            httpsPort = tn;
        } else {
            httpsPort = 8443;
        }


        //
        val = configReader.getString("netty.https.server.keyStorePath");
        if (null != val) {
            try {
                ServerConfig.setKeyStorePath(val.toString());
            } catch (Exception e) {
                logger.error("Parse keyStorePath fail!", e);
            }
        }
        //
        val = configReader.getString("netty.https.server.keyStorePassword");
        if (null != val) {
            try {
                ServerConfig.setKeyStorePassword(val.toString());
            } catch (Exception e) {
                logger.error("Parse getKeyStorePassword fail!", e);
            }
        }
        //
        val = configReader.getString("netty.https.server.keyPassword");
        if (null != val) {
            try {
                ServerConfig.setKeyPassword(val.toString());
            } catch (Exception e) {
                logger.error("Parse keyPassword fail!", e);
            }
        }

    }

    public static String getStaticResourcesFolder() {
        return staticResourcesFolder;
    }
}
