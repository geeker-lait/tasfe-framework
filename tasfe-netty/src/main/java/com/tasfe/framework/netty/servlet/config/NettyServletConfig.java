package com.tasfe.framework.netty.servlet.config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

public class NettyServletConfig implements ServletConfig {

    private ServletContext servletContext;

    public NettyServletConfig(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public String getServletName() {
        return "tasfe";
    }

    @Override
    public String getInitParameter(String name) {
        return servletContext.getInitParameter(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return servletContext.getInitParameterNames();
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

}
