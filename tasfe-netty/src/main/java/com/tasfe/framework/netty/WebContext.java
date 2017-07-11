package com.tasfe.framework.netty;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

public interface WebContext {

    /**
     * Provides access to the HttpServlet used by the servlet implementation. It
     * is this servlet that will be used by cruzeira to handle all incoming
     * request.
     */
    HttpServlet getHttpServlet();

    /**
     * Executes a graceful shutdown in the servlet world.
     */
    void shutdown();

    /**
     * Return the ServletContext used by the servlet
     */
    ServletContext getServletContext();

}
