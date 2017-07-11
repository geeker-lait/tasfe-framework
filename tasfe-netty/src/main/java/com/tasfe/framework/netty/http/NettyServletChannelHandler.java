package com.tasfe.framework.netty.http;

import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;

import com.tasfe.framework.netty.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class NettyServletChannelHandler extends NettyAbstractServletChannelHandler {

    final Logger logger = LoggerFactory.getLogger(NettyServletChannelHandler.class);

    public NettyServletChannelHandler(WebContext webContext) {
        super(webContext);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        FullHttpRequest request = (FullHttpRequest) msg;
        // is100ContinueExpected(request);
        StringBuilder buf = new StringBuilder();

        ServletOutput servletOutput;
        try {
            servletOutput = doServlet(ctx, request, buf);
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            sendError(ctx, INTERNAL_SERVER_ERROR);
            return;
        }

        if (servletOutput != null) {
            if (servletOutput.isAsync()) {
                // async - send upstream
                ctx.fireChannelRead(servletOutput);
            } else {
                writeResponse(ctx, buf, servletOutput.getServletRequest(), servletOutput.getServletResponse());
            }
        }
    }

}
