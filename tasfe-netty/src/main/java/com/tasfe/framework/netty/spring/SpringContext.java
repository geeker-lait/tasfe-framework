package com.tasfe.framework.netty.spring;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.tasfe.framework.netty.WebContext;
import com.tasfe.framework.netty.servlet.config.NettyServletConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.tasfe.framework.netty.servlet.NettyServletContext;

public class SpringContext implements WebContext {

    final Logger logger = LoggerFactory.getLogger(SpringContext.class);

    private WebApplicationContext webAppContext;
    private DispatcherServlet dispatcherServlet;
    private ServletConfig servletConfig;
    private ServletContext servletContext;

    public SpringContext() throws ServletException {
        this("classpath:app-context.xml");
    }

    public SpringContext(String configLocation) throws ServletException {
        logger.debug("Initializing spring context");
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        webAppContext = appContext;
        ContextLoader contextLoader = new ContextLoader(webAppContext);
        servletContext = new NettyServletContext(this);
        servletContext.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, configLocation);
        contextLoader.initWebApplicationContext(servletContext);

        dispatcherServlet = new DispatcherServlet(webAppContext);

        servletConfig = new NettyServletConfig(servletContext);
        dispatcherServlet.init(servletConfig);
    }

    public void shutdown() {
        logger.debug("Spring context shutdown in progress");
        AbstractApplicationContext appContext = (AbstractApplicationContext) webAppContext;
        appContext.destroy();
        dispatcherServlet.destroy();
    }

    public HttpServlet getHttpServlet() {
        return dispatcherServlet;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

}
