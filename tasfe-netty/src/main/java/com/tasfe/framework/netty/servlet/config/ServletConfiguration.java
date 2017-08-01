package com.tasfe.framework.netty.servlet.config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.tasfe.framework.netty.servlet.impls.ServletConfigImpl;

public class ServletConfiguration extends HttpComponentConfigurationAdapter<HttpServlet, ServletConfigImpl> {

    public ServletConfiguration(Class<? extends HttpServlet> servletClazz, String... urlPatterns) {
        super(servletClazz, urlPatterns);
    }

    public ServletConfiguration(Class<? extends HttpServlet> componentClazz) {
        super(componentClazz);
    }

    public ServletConfiguration(HttpServlet component, String... urlPatterns) {
        super(component, urlPatterns);
    }

    public ServletConfiguration(HttpServlet servlet) {
        super(servlet);
    }

    @Override
    protected void doInit() throws ServletException {
        this.component.init(this.config);
    }

    @Override
    protected void doDestroy() throws ServletException {
        this.component.destroy();
    }

    @Override
    protected ServletConfigImpl newConfigInstance(Class<? extends HttpServlet> componentClazz) {
        return new ServletConfigImpl(this.component.getClass().getSimpleName());
    }

    public ServletConfiguration addInitParameter(String name, String value) {
        super.addConfigInitParameter(name, value);
        return this;
    }
}
