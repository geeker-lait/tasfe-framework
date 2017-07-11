package com.tasfe.framework.netty;

import com.tasfe.framework.netty.annotation.Env;
import com.tasfe.framework.netty.annotation.NettyBootstrap;
import com.tasfe.framework.netty.utils.ResourcesUtil;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author liat.zhang@gmail.com
 * @date 2016年9月15日-下午9:19:23
 * @description <code>NettyServerBootstrap</code>服务器引导程序类，用于调度服务器启动接口
 */
@NettyBootstrap
public class TasfeApplication {
    /**
     * @param args
     * @description <code>入口程序</code>
     */
    public static void main(String[] args) {
        run(null, args);
    }

    public static void run(Class<?> clazz, String[] args) {
        //需要手动扫描加载
        try {
            File file = ResourcesUtil.getFile("classpath*:/*.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        NettyServerBootstrap nettyServerBootstrap = NettyServerBootstrap.createServer();
        if (null != clazz) {
            NettyBootstrap nb = clazz.getAnnotation(NettyBootstrap.class);
            Env env = clazz.getAnnotation(Env.class);
            if (env != null && nb != null) {
                String springApplicationContext = nb.springApplicationContext();
                String springServletContext = nb.springServletContext();
                String serverProperties = nb.serverProperties();
                nettyServerBootstrap
                        .setSpringApplicationContext(springApplicationContext)
                        .setSpringServletContext(springServletContext)
                        .setServerProperties(serverProperties)
                        .setEnv(env.profile());
            } else {
                throw new RuntimeException("please config env profile !");
            }

        }
        nettyServerBootstrap.parseCommondArguments(args).start();
    }

    public void run() {
        // 创建->设置application父容器->设置springmvc子容器->设置server参数->启动
        //NettyServerBootstrap.createServer().setSpringApplicationContext(springApplicationContext).setSpringServletContext(springServletContext).parseCommondArguments(args).start();
    }
}
