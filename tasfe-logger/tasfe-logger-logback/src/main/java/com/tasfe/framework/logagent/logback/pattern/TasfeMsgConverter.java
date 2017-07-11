package com.tasfe.framework.logagent.logback.pattern;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.tasfe.framework.logagent.logback.utils.Constants;

/**
 * Created by lait on 2017/6/8.
 */
public class TasfeMsgConverter extends ClassicConverter {
    public TasfeMsgConverter() {
    }

    public String convert(ILoggingEvent event) {
        String message = event.getFormattedMessage();
        return message.replaceAll(" ", Constants.SYMBOL_SPACE);
    }
}
