package com.tasfe.framework.netty.servlet.interceptor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public interface HttpServletInterceptor {

    void onRequestReceived(ChannelHandlerContext ctx, HttpRequest e);

    void onRequestSuccessed(ChannelHandlerContext ctx, HttpRequest e, HttpResponse response);

    void onRequestFailed(ChannelHandlerContext ctx, Throwable e, HttpResponse response);

}
