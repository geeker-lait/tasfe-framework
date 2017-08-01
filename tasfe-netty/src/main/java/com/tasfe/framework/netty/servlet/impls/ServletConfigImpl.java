package com.tasfe.framework.netty.servlet.impls;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class ServletConfigImpl extends ConfigAdapter implements ServletConfig {

    public ServletConfigImpl(String servletName) {
        super(servletName);
    }

    @Override
    public String getServletName() {
        return super.getOwnerName();
    }

    @Override
    public ServletContext getServletContext() {
        return ServletContextImpl.get();
    }

}
