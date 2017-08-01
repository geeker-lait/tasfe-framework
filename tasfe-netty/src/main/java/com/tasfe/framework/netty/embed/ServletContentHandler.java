package com.tasfe.framework.netty.embed;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

/**
 * {@link io.netty.channel.ChannelInboundHandler} responsible for initial request handling, and getting received
 * {@link HttpContent} messages to the {@link HttpContentInputStream} for the request.
 */
class ServletContentHandler extends ChannelInboundHandlerAdapter {
    private final NettyEmbeddedContext servletContext;
    private HttpContentInputStream inputStream; // FIXME this feels wonky, need a better approach

    ServletContentHandler(NettyEmbeddedContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        inputStream = new HttpContentInputStream(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, false);
            HttpHeaders.setKeepAlive(response, HttpHeaders.isKeepAlive(request));
            NettyHttpServletResponse servletResponse = new NettyHttpServletResponse(ctx, servletContext, response);
            NettyHttpServletRequest servletRequest = new NettyHttpServletRequest(ctx, servletContext, request, servletResponse, inputStream);
            if (HttpHeaders.is100ContinueExpected(request)) {
                ctx.write(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE), ctx.voidPromise());
            }
            ctx.fireChannelRead(servletRequest);
        }
        if (msg instanceof HttpContent) {
            inputStream.addContent((HttpContent) msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        inputStream.close();
    }
}