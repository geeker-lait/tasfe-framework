package com.tasfe.framework.netty.servlet.interceptor;

import static io.netty.handler.codec.http.HttpHeaderNames.SET_COOKIE;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import com.tasfe.framework.netty.servlet.impls.HttpSessionImpl;
import com.tasfe.framework.netty.servlet.session.HttpSessionStore;
import com.tasfe.framework.netty.servlet.session.HttpSessionThreadLocal;
import com.tasfe.framework.netty.utils.Utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;

public class HttpSessionInterceptor implements HttpServletInterceptor {

    private boolean sessionRequestedByCookie = false;

    public HttpSessionInterceptor(HttpSessionStore sessionStore) {
        HttpSessionThreadLocal.setSessionStore(sessionStore);
    }

    @Override
    public void onRequestReceived(ChannelHandlerContext ctx, HttpRequest request) {

        HttpSessionThreadLocal.unset();

        Collection<Cookie> cookies = Utils.getCookies(HttpSessionImpl.SESSION_ID_KEY, request);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String jsessionId = cookie.value();
                HttpSession s = HttpSessionThreadLocal.getSessionStore().findSession(jsessionId);
                if (s != null) {
                    HttpSessionThreadLocal.set(s);
                    this.sessionRequestedByCookie = true;
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestSuccessed(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response) {

        HttpSession s = HttpSessionThreadLocal.get();
        if (s != null && !this.sessionRequestedByCookie) {
            HttpHeaders.addHeader(response, SET_COOKIE,
                    ServerCookieEncoder.LAX.encode(HttpSessionImpl.SESSION_ID_KEY, s.getId()));
        }

    }

    @Override
    public void onRequestFailed(ChannelHandlerContext ctx, Throwable e, HttpResponse response) {
        this.sessionRequestedByCookie = false;
        HttpSessionThreadLocal.unset();
    }

}
