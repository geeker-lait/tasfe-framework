package com.tasfe.framework.netty.initializer;

import com.tasfe.framework.netty.http.HttpServletHandler;

import com.tasfe.framework.netty.servlet.ServletWebApp;
import com.tasfe.framework.netty.servlet.config.WebAppConfiguration;
import com.tasfe.framework.netty.servlet.interceptor.ChannelInterceptor;
import com.tasfe.framework.netty.servlet.session.HttpSessionStore;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tasfe.framework.netty.config.ServerConfig;
import com.tasfe.framework.netty.servlet.session.DefaultHttpSessionStore;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * @author liat.zhang@gmail.com
 * @date 2016年9月16日-下午10:01:12
 * @description <code>HttpChannelInitializer</code>用于注册http请求相关的Handler
 */
public class HttpChannelInitializer extends ChannelInitializerWrapper {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DefaultEventExecutor eventExecutor = new DefaultEventExecutor();

    private ChannelGroup channels = new DefaultChannelGroup(eventExecutor);

    private DefaultEventExecutorGroup businessExecutor;

    private HttpSessionWatchdog watchdog;

    // 默认大小1M
    private int maxConentLength = 1048576;

    public HttpChannelInitializer(WebAppConfiguration config) {
        // 初始化应用程序。
        ServletWebApp webapp = ServletWebApp.get();
        webapp.init(config, channels);
        // 设置文件上传大小限制
        this.maxConentLength = config.getMaxContentLength();
        // 用户会话监控器。
        /*businessExecutor = new DefaultEventExecutorGroup(ServerConfig.getBusinessThreadNumber());
		this.watchdog = new HttpSessionWatchdog();
		businessExecutor.execute(watchdog);*/
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // SSL的安全链接
        if (ServerConfig.isSsl()) {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            KeyStore ks = KeyStore.getInstance("JKS");
            String keyStorePath = ServerConfig.getKeyStorePath();
            String keyStorePassword = ServerConfig.getKeyStorePassword();
            ks.load(new FileInputStream(keyStorePath), keyStorePassword.toCharArray());
            String keyPassword = ServerConfig.getKeyPassword();
            kmf.init(ks, keyPassword.toCharArray());
            sslcontext.init(kmf.getKeyManagers(), null, null);
            SSLEngine sslEngine = sslcontext.createSSLEngine();
            sslEngine.setUseClientMode(false);
            sslEngine.setNeedClientAuth(false);
            /**
             * 务必放在第一位
             */
            pipeline.addLast(new SslHandler(sslEngine));
            logger.info("initChannel: addLast SslHandler");
            /**
             * Generates a temporary self-signed certificate for testing purposes.
             */
			/*SelfSignedCertificate ssc = new SelfSignedCertificate();
			SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			//SslContext sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
			if (sslCtx != null) {
				pipeline.addLast(sslCtx.newHandler(ch.alloc()));
			}*/
        }
        // Register HTTP handler chain.
        this.appendHttpPipeline(pipeline);
    }

    public void shutdown() {
        //this.watchdog.stopWatching();
        ServletWebApp.get().destroy();
        this.channels.close().awaitUninterruptibly();
    }

    public final ChannelPipeline appendHttpPipeline(ChannelPipeline channelPipeline) {
        // 服务端，对响应编码。属于ChannelOutboundHandler，逆序执行
        channelPipeline.addLast("encoder", new HttpResponseEncoder());

        // 服务端，对请求解码。属于ChannelIntboundHandler，按照顺序执行
        channelPipeline.addLast("decoder", new HttpRequestDecoder());
        //即通过它可以把 HttpMessage 和 HttpContent 聚合成一个 FullHttpRequest,并定义可以接受的数据大小，在文件上传时，可以支持params+multipart
        channelPipeline.addLast("aggregator", new HttpObjectAggregator(maxConentLength));
        //块写入写出Handler
        channelPipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

        // 对传输数据进行压缩，这里在客户端需要解压缩处理
        // channelPipeline.addLast("deflater", new HttpContentCompressor());

        HttpServletHandler servletHandler = new HttpServletHandler();
        servletHandler.addInterceptor(new ChannelInterceptor());
        //servletHandler.addInterceptor(new HttpSessionInterceptor(getHttpSessionStore()));
        // 自定义Handler
        channelPipeline.addLast("handler", servletHandler);
        // 异步
        // channelPipeline.addLast(businessExecutor, new AsyncHttpServletHandler());
        return channelPipeline;
    }

    protected HttpSessionStore getHttpSessionStore() {
        return new DefaultHttpSessionStore();
    }

    private class HttpSessionWatchdog implements Runnable {

        private boolean shouldStopWatching = false;

        @Override
        public void run() {
            while (!shouldStopWatching) {

                try {
                    HttpSessionStore store = getHttpSessionStore();
                    if (store != null) {
                        store.destroyInactiveSessions();
                    }
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    return;
                }

            }

        }
        public void stopWatching() {
            this.shouldStopWatching = true;
        }
    }
}
