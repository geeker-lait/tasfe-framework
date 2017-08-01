package com.tasfe.framework.netty.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lait.zhang@gmail.com
 * @Date Oct 13, 2016
 * @Description:<code></code>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NettyBootstrap {
    String springApplicationContext() default "classpath:/*-application.xml";

    String springServletContext() default "classpath:/*-web.xml";

    String serverProperties() default "server.properties";
}
