package com.tasfe.framework.netty.http;

import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tasfe.framework.netty.WebContext;
import com.tasfe.framework.netty.spring.ThreadLocalAsyncExecutor;

import io.netty.channel.ChannelHandlerContext;

public class NettyAsyncServletChannelHandler extends NettyAbstractServletChannelHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public NettyAsyncServletChannelHandler(WebContext webContext) {
        super(webContext);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.info("Async message received");

        ServletOutput servletOutput = (ServletOutput) msg;
        StringBuilder buf = new StringBuilder();

        try {
            Runnable asyncRunnable = ThreadLocalAsyncExecutor
                    .getAndRemoveAsyncRunnable(servletOutput.getServletResponse());
            logger.info("Request, response, runnable: {}, {}, {}", servletOutput.getServletRequest(),
                    servletOutput.getServletResponse(), asyncRunnable);
            asyncRunnable.run();
            servletOutput = doServlet(ctx, buf, servletOutput.getServletRequest(), servletOutput.getServletResponse());
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            sendError(ctx, INTERNAL_SERVER_ERROR);
            return;
        }

        if (servletOutput != null) {
            writeResponse(ctx, buf, servletOutput.getServletRequest(), servletOutput.getServletResponse());
        }
    }

    @Override
    protected boolean isCheckAsync() {
        return false;
    }

}
