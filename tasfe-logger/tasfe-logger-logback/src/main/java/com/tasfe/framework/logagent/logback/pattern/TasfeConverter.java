package com.tasfe.framework.logagent.logback.pattern;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TasfeConverter extends ClassicConverter {

    static String hostName;
    static String localIp;

    static {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            hostName = ia.getHostName();
            localIp = System.getenv().get("DOCKER_HOST_IP");
            if (StringUtils.isBlank(localIp))
                localIp = ia.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String convert(ILoggingEvent event) {
        // TODO Auto-generated method stub
        return localIp;
    }

}
