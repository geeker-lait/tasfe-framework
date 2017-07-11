package com.tasfe.framework.netty.servlet.exception;

/**
 * @author liat.zhang@gmail.com
 * @date 2016年9月14日-下午9:21:40
 * @description <code>NettyServletRuntimeException</code>
 */
public class NettyServletRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NettyServletRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NettyServletRuntimeException(String message) {
        super(message);
    }

}
