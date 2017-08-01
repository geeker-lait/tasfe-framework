package com.tasfe.framework.netty.servlet.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tasfe.framework.netty.servlet.impls.ServletContextImpl;
import com.tasfe.framework.netty.utils.Utils;

public class ServletContextListenerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ServletContextListenerConfiguration.class);

    private ServletContextListener listener;

    private boolean initialized = false;

    public ServletContextListenerConfiguration(Class<? extends ServletContextListener> clazz) {
        this(Utils.newInstance(clazz));
    }

    public ServletContextListenerConfiguration(ServletContextListener listener) {
        this.listener = listener;
    }

    public ServletContextListener getListener() {
        return listener;
    }

    public void init() {
        try {

            log.debug("Initializing listener: {}", this.listener.getClass());

            this.listener.contextInitialized(new ServletContextEvent(ServletContextImpl.get()));
            this.initialized = true;

        } catch (Exception e) {

            this.initialized = false;
            log.error("Listener '" + this.listener.getClass() + "' was not initialized!", e);
        }
    }

    public void destroy() {
        try {

            log.debug("Destroying listener: {}", this.listener.getClass());

            this.listener.contextDestroyed(new ServletContextEvent(ServletContextImpl.get()));
            this.initialized = false;

        } catch (Exception e) {

            this.initialized = false;
            log.error("Listener '" + this.listener.getClass() + "' was not destroyed!", e);
        }
    }

    public boolean isInitialized() {
        return initialized;
    }
}
