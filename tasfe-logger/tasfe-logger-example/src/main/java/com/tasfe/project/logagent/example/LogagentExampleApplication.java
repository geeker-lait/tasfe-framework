package com.tasfe.project.logagent.example;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.NettyBootstrap;

/**
 * Hello world!
 */
@NettyBootstrap(springApplicationContext = "classpath:/logagent-example-application.xml", springServletContext = "classpath:/logagent-example-servlet.xml")
public class LogagentExampleApplication {
    public static void main(String[] args) throws Exception{
        TasfeApplication.run(LogagentExampleApplication.class, args);
    }
}
