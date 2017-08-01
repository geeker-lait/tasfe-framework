package com.tasfe.framework.netty.embed;

import javax.servlet.*;
import java.io.IOException;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A very simple {@link FilterChain} implementation.
 *
 * @author lait.zhang@gmail.com
 */
class SimpleFilterChain implements FilterChain {
    private final Iterator<Filter> filterIterator;
    private final Servlet servlet;

    SimpleFilterChain(Servlet servlet, Iterable<Filter> filters) throws ServletException {
        this.filterIterator = checkNotNull(filters).iterator();
        this.servlet = checkNotNull(servlet);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (filterIterator.hasNext()) {
            Filter filter = filterIterator.next();
            filter.doFilter(request, response, this);
        } else {
            servlet.service(request, response);
        }
    }
}
