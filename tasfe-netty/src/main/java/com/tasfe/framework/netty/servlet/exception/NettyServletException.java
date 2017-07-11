package com.tasfe.framework.netty.servlet.exception;

/**
 * @author liat.zhang@gmail.com
 * @date 2016年9月14日-下午9:19:40
 * @description <code>NettyServletException</code>
 */
public class NettyServletException extends Exception {

    private static final long serialVersionUID = 1L;

    public NettyServletException(String message, Throwable cause) {
        super(message, cause);
    }

    public NettyServletException(String message) {
        super(message);
    }

}
