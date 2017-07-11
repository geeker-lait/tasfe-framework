package com.tasfe.framework.netty.servlet.interceptor;

import com.tasfe.framework.netty.ChannelThreadLocal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class ChannelInterceptor implements HttpServletInterceptor {

    @Override
    public void onRequestFailed(ChannelHandlerContext ctx, Throwable e, HttpResponse response) {
        ChannelThreadLocal.unset();
    }

    @Override
    public void onRequestReceived(ChannelHandlerContext ctx, HttpRequest e) {
        ChannelThreadLocal.set(ctx.channel());
    }

    @Override
    public void onRequestSuccessed(ChannelHandlerContext ctx, HttpRequest e, HttpResponse response) {
        ChannelThreadLocal.unset();
    }

}
