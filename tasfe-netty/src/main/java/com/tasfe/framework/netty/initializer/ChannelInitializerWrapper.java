package com.tasfe.framework.netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author liat.zhang@gmail.com
 * @date 2016年9月16日-下午10:00:42
 * @description <code>ChannelInitializerWrapper</code>是Channel Handler与业务容器的包装类
 */
public abstract class ChannelInitializerWrapper extends ChannelInitializer<SocketChannel> {

    public abstract void shutdown();
}
