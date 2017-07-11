package com.tasfe.framework.logagent.logback.pattern;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.UUID;

/**
 * Created by lait on 2017/6/7.
 */
public class PrimarykeyConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
