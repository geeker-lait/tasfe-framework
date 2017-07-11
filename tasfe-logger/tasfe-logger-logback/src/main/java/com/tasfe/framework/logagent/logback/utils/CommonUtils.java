package com.tasfe.framework.logagent.logback.utils;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lait on 2017/5/25.
 */
public class CommonUtils {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS+0800";
    private static InetAddress addr;

    public static InetAddress getLocalAddress() {
        if (addr == null) {
            try {
                addr = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return addr;
    }

    public static String formatDate() {
        DateFormat sf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return sf.format(new Date()).replaceAll(" ","T");
    }
}
