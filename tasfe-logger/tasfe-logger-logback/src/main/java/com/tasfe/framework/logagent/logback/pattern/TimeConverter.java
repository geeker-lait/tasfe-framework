package com.tasfe.framework.logagent.logback.pattern;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.tasfe.framework.logagent.logback.utils.CommonUtils;

/**
 * Created by lait on 2017/6/15.
 */
public class TimeConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return CommonUtils.formatDate();
    }

}
