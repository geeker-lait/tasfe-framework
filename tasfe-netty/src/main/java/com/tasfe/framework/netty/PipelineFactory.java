package com.tasfe.framework.netty;

import com.tasfe.framework.netty.http.NettyAsyncServletChannelHandler;
import com.tasfe.framework.netty.http.NettyServletChannelHandler;
import com.tasfe.framework.netty.spring.SpringContext;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

public class PipelineFactory extends ChannelInitializer<SocketChannel> {

    private WebContext webContext;
    private int asyncPoolSize;

    public PipelineFactory(int asyncPoolSize) throws Exception {
        this.webContext = new SpringContext();
        this.asyncPoolSize = asyncPoolSize;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new HttpServerCodec());

        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new NettyServletChannelHandler(webContext));
        pipeline.addLast(new DefaultEventExecutorGroup(asyncPoolSize), new NettyAsyncServletChannelHandler(webContext));
    }

    public void shutdown() {
        webContext.shutdown();
    }

}
