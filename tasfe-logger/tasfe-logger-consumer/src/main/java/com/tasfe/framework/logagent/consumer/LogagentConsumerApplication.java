package com.tasfe.framework.logagent.consumer;

import com.tasfe.framework.netty.TasfeApplication;
import com.tasfe.framework.netty.annotation.NettyBootstrap;

@NettyBootstrap(springApplicationContext = "classpath:/logagent-consumer-application.xml")
public class LogagentConsumerApplication {

    public static void main(String[] args) {
        TasfeApplication.run(LogagentConsumerApplication.class, args);

    }

}
