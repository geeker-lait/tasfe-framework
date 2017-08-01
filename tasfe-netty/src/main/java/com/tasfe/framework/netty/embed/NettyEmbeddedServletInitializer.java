package com.tasfe.framework.netty.embed;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.concurrent.EventExecutorGroup;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link ChannelInitializer} for {@link org.springframework.boot.context.embedded.netty.NettyEmbeddedServletContainer}.
 *
 * @author lait.zhang@gmail.com
 */
class NettyEmbeddedServletInitializer extends ChannelInitializer<SocketChannel> {
    private final EventExecutorGroup servletExecutor;
    private final RequestDispatcherHandler requestDispatcherHandler;
    private final NettyEmbeddedContext servletContext;

    NettyEmbeddedServletInitializer(EventExecutorGroup servletExecutor, NettyEmbeddedContext servletContext) {
        this.servletContext = servletContext;
        this.servletExecutor = checkNotNull(servletExecutor);
        requestDispatcherHandler = new RequestDispatcherHandler(servletContext);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast("codec", new HttpServerCodec(4096, 8192, 8192, false));
        p.addLast("servletInput", new ServletContentHandler(servletContext));
        p.addLast(servletExecutor, "filterChain", requestDispatcherHandler);
    }
}
