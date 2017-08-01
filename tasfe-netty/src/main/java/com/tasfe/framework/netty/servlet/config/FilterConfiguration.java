package com.tasfe.framework.netty.servlet.config;

import javax.servlet.Filter;
import javax.servlet.ServletException;

import com.tasfe.framework.netty.servlet.impls.FilterConfigImpl;

public class FilterConfiguration extends HttpComponentConfigurationAdapter<Filter, FilterConfigImpl> {

    public FilterConfiguration(Class<? extends Filter> servletClazz, String... urlPatterns) {
        super(servletClazz, urlPatterns);
    }

    public FilterConfiguration(Class<? extends Filter> componentClazz) {
        super(componentClazz);
    }

    public FilterConfiguration(Filter component, String... urlPatterns) {
        super(component, urlPatterns);
    }

    public FilterConfiguration(Filter servlet) {
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
    protected FilterConfigImpl newConfigInstance(Class<? extends Filter> componentClazz) {
        return new FilterConfigImpl(componentClazz.getName());
    }

    public FilterConfiguration addInitParameter(String name, String value) {
        super.addConfigInitParameter(name, value);
        return this;
    }

}
