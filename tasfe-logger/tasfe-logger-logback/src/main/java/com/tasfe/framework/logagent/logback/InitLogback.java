package com.tasfe.framework.logagent.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;

/**
 * Created by lait on 2017/6/6.
 */
@Component
public class InitLogback {

    @PostConstruct
    public void initializeLogback() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        InputStream is = InitLogback.class.getClassLoader().getResourceAsStream("tasfe-logback.xml");
        if (is == null)
            return;
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();

        try {
            configurator.doConfigure(is);
        } catch (JoranException e) {
            e.printStackTrace();
        }
    }
}
