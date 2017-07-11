package com.tasfe.framework.netty;

import com.tasfe.framework.netty.config.ServerConfig;
import com.tasfe.framework.netty.initializer.ChannelInitializerWrapper;
import com.tasfe.framework.netty.initializer.HttpChannelInitializer;
import com.tasfe.framework.netty.servlet.config.ServletConfiguration;
import com.tasfe.framework.netty.servlet.config.WebAppConfiguration;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author liat.zhang@gmail.com
 * @date 2016年9月15日-下午9:19:23
 * @description <code>NettyServerBootstrap</code>服务器引导程序类，用于调度服务器启动接口
 */
public class NettyServerBootstrap {

    /**
     * 日志对象
     */
    final static Logger logger = LoggerFactory.getLogger(NettyServerBootstrap.class);
    /**
     * 定义spring 全局配置文件 父容器  默认 * 代表项目名称 如：tasfe-demo-application.xml
     */
    public static final String SPRING_APPLICATION_CONFIG = "classpath*:/tasfe-application.xml";
    /**
     * 定义spring 全局配置文件servletmvc 子容器 默认 * 代表项目名称 如：tasfe-demo-servlet.xml
     */
    public static final String SPRING_SERVLET_CONFIG = "classpath*:/tasfe-servlet.xml";
    public static final String SERVER_PROPERTIES = "server.properties";

    /**
     * 服务器支持的命令行参数选项
     */
    private Options options;

    /**
     * Socket链接轮询器（acceptor thread loop, 默认线程数为处理器*2 ）
     */
    private EventLoopGroup acceptorGroup;

    /**
     * IO处理轮询器（work thread loop, 默认线程数为处理器*2 ）
     */
    private EventLoopGroup workerGroup;

    /**
     * 当前服务器待加载的ChannelInitializer
     */
    private ChannelInitializerWrapper currentInitializer;

    private String springApplicationContext = SPRING_APPLICATION_CONFIG;
    private String springServletContext = SPRING_SERVLET_CONFIG;
    private String serverProperties = SERVER_PROPERTIES;
    private String env;

    /**
     * 自定义的端口号
     */
    private Integer port;

    /**
     * 功能描述：缺省构造函数。
     */
    private NettyServerBootstrap() {
        this.registerCommandLineOptions();
    }

    /**
     * @return
     * @description <code>创建HTTP服务器实例</code>
     */
    public static NettyServerBootstrap createServer() {
        return new NettyServerBootstrap();
    }

    /**
     * @description <code>启动服务器</code>
     */
    public void start() {
        try {
            // 初始化配置参数
            ServerConfig.init(serverProperties);
            // 绑定服务器端口
            int port = ServerConfig.getPort();
            if (this.port != null && 0 < this.port && this.port < 65535) {
                port = this.port;
            }
            ServerBootstrap serverBootstrap = this.startup();
            // 绑定http端口
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            logger.info("Running {} netty server on {} ...", ServerConfig.getProtocol(), port);
            System.err.println("Connect to http" + "://127.0.0.1:" + port + '/');
            // 绑定https,默认是8443
            if (ServerConfig.isSsl()) {
                channelFuture = serverBootstrap.bind(ServerConfig.getHttpsPort()).sync();
                System.err.println("Connect to https" + "://127.0.0.1:" + ServerConfig.getHttpsPort() + '/');
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.shutdownServer();
        }
    }

    /**
     * @return
     * @description <code>执行服务器引导程序</code>
     */
    private ServerBootstrap startup() {
        ServerBootstrap serverBootstrap = null;
        // 自定义线程数
        int bossThreadNumber = ServerConfig.getNioThreadNumber();
        int workerThreadNumber = ServerConfig.getBusinessThreadNumber();
        int connectionTimeout = ServerConfig.getConnectionTimeout();
        String staticResoucesFolder = ServerConfig.getStaticResourcesFolder();
        // 优化配置参数
        //int channelOptionSoBlocking = ServerConfig.getChannelOptionSoBlocking();
        try {
            boolean linux = getOSMatches("Linux") || getOSMatches("LINUX");
            acceptorGroup = linux ? new EpollEventLoopGroup(bossThreadNumber) : new NioEventLoopGroup(bossThreadNumber);
            workerGroup = linux ? new EpollEventLoopGroup(workerThreadNumber) : new NioEventLoopGroup(workerThreadNumber);
            // 创建netty服务端引导对象
            serverBootstrap = new ServerBootstrap();
            // 设置接收器组以及请求轮询器组
            serverBootstrap.group(acceptorGroup, workerGroup);
            // 设置服务端soket通道
            serverBootstrap.channel(linux ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
            serverBootstrap
                    /**
                     * 在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，
                     * 同时，对方接收到数据，也需要发送ACK表示确认。
                     * 为了尽可能的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。这里就涉及到一个名为Nagle的算法，
                     * 该算法的目的就是为了尽可能发送大块数据，避免网络中充斥着许多小数据块。
                     *
                     * TCP_NODELAY就是用于启用或关于Nagle算法。
                     * 如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；
                     * 如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
                     */
                    .option(ChannelOption.TCP_NODELAY, true)
                    /**
                     * 超时时间 ，这个设置保证当达到60000(1分钟)毫秒时，就释放channel,这样内存就不会需要等待很长时间释放
                     */
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                    /**
                     *  合理设置接收和发送缓冲区容量：
                     *  JDK原生的NIO类库使用的是java.nio.ByteBuffer,它实际是一个长度固定的Byte数组，我们都知道数组无法动态扩容，
                     *  ByteBuffer也有这个限制。
                     *  如果容量无法动态扩展会给用户带来一些麻烦，例如由于无法预测每条消息报文的长度，可能需要预分配一个比较大的ByteBuffer，
                     *  这通常也没有问题。但是在海量推送服务系统中，这会给服务端带来沉重的内存负担。假设单条推送消息最大上限为10K，
                     *  消息平均大小为5K，为了满足10K消息的处理，ByteBuffer的容量被设置为10K，这样每条链路实际上多消耗了5K内存，
                     *  如果长链接链路数为100万，每个链路都独立持有ByteBuffer接收缓冲区，则额外损耗的总内存 Total(M) = 1000000 * 5K = 4882M。
                     *  内存消耗过大，不仅仅增加了硬件成本，而且大内存容易导致长时间的Full GC，对系统稳定性会造成比较大的冲击。
                     *
                     *  Netty提供的ByteBuf支持容量动态调整，对于接收缓冲区的内存分配器，Netty提供了两种：
                     *  FixedRecvByteBufAllocator：
                     *  固定长度的接收缓冲区分配器，由它分配的ByteBuf长度都是固定大小的，并不会根据实际数据报的大小动态收缩。
                     *  但是，如果容量不足，支持动态扩展。动态扩展是Netty ByteBuf的一项基本功能，与ByteBuf分配器的实现没有关系；
                     *  AdaptiveRecvByteBufAllocator：
                     *  容量动态调整的接收缓冲区分配器，它会根据之前Channel接收到的数据报大小进行计算，如果连续填充满接收缓冲区的可写空间，
                     *  则动态扩展容量。如果连续2次接收到的数据报都小于指定值，则收缩当前的容量，以节约内存。
                     *
                     *  通过设置该参数，使即接收缓冲器,自动扩容。
                     *  另外值得注意的是，无论是接收缓冲区还是发送缓冲区，缓冲区的大小建议设置为消息的平均大小，不要设置成最大消息的上限，
                     *  这会导致额外的内存浪费。通过如下方式可以设置接收缓冲区的初始大小。
                     */
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    /**
                     * 内存池相关优化：
                     * Netty默认不使用内存池，需要在创建客户端或者服务端的时候进行指定,
                     * 使用内存池之后，内存的申请和释放必须成对出现，即retain()和release()要成对出现，否则会导致内存泄露。
                     * ReferenceCountUtil.release(msg)对接收缓冲区ByteBuf进行内存释放，否则它会被认为仍然在使用中，这样会导致内存泄露。
                     */
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    /**
                     * option()是提供给NioServerSocketChannel用来接收进来的连接,也就是boss线程。
                     *
                     * 设置TCP的参数，这里设置了套接字的最大连接个数。
                     * BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
                     * 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
                     */
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    /**
                     * 地址复用
                     */
                    .option(ChannelOption.SO_REUSEADDR, true)
                    /**
                     * childOption()是提供给由父管道ServerChannel接收到的连接，也就是worker线程
                     */
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 配置Netty日志级别
            serverBootstrap.handler(new LoggingHandler(LogLevel.ERROR));
            /**
             * 构建web.xml
             */
            if (env != null && env.endsWith(".xml")) {
                springApplicationContext += ("," + env);
            }
            WebAppConfiguration webapp = new WebAppConfiguration()
                    // 项目名称
                    .setName("netty-web-server")
                    // 设置session超时时间
                    .setSessionTimeout(ServerConfig.getSessionTimeout())
                    // 静态资源名称
                    .setStaticResourcesFolder(null == staticResoucesFolder ? "/statics" : staticResoucesFolder)
                    // 文件大小
                    .setMaxContentLength(ServerConfig.getMaxContentLength())
                    .addContextParameter(ContextLoader.CONFIG_LOCATION_PARAM, springApplicationContext)
                    .addServletContextListener(ContextLoaderListener.class);

            // 缺省为 /WEB-INF/DispatcherServlet-servlet.xml,如果有配置spring-mvc 子上下文环境,则设置
            ServletConfiguration sc = new ServletConfiguration(DispatcherServlet.class)
                    .addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, springServletContext);
            webapp.addServletConfigurations(sc);

            // 注册dubbo servlet 文件上传
            /*ServletConfiguration dubboServletConfiguration = new ServletConfiguration(com.alibaba.dubbo.remoting.http.servlet.DispatcherServlet.class)
					.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, springServletContext);
			webapp.addServletConfigurations(dubboServletConfiguration);*/
            //com.alibaba.dubbo.remoting.http.servlet.DispatcherServlet dubboServlet = new com.alibaba.dubbo.remoting.http.servlet.DispatcherServlet();

            // .addFilterConfigurations(new
            // FilterConfiguration(CharacterEncodingFilter.class)
            // .addInitParameter("encoding", ServerConfig.getCharsetName())
            // .addInitParameter("forceEncoding", "true");

            // 创建Http通道初始化器。
            currentInitializer = new HttpChannelInitializer(webapp);
            serverBootstrap.childHandler(currentInitializer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverBootstrap;
    }


    public NettyServerBootstrap setEnv(String env) {
        this.env = env;
        return this;
    }

    /**
     * @param args
     * @description <code>解析命令行参数</code>
     */
    public NettyServerBootstrap parseCommondArguments(String[] args) {
        try {
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(options, args);

            // 解析端口参数
            if (cmd.hasOption("port")) {
                try {
                    this.port = Integer.valueOf(cmd.getOptionValue("port"));
                    ServerConfig.setPort(port);
                } catch (Exception e) {
                    logger.error("Parse port fail!", e);
                }
            }

            // TODO 解析其他参数
        } catch (Exception e) {
            logger.error("Error on cli", e);
        }
        return this;
    }

    public void shutdownServer() {
        if (null != currentInitializer) {
            currentInitializer.shutdown();
        }
        if (acceptorGroup != null) {
            acceptorGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    private void registerCommandLineOptions() {
        options = new Options();

        // 服务器端口选项
        options.addOption("port", true, "sever port");
    }

    /**
     * @param springApplicationContext
     * @return
     * @description <code>设置父容器</code>
     */
    public NettyServerBootstrap setSpringApplicationContext(String springApplicationContext) {
        this.springApplicationContext = springApplicationContext;
        return this;
    }

    /**
     * @param springServletContext
     * @return
     * @description <code>设置子容器</code>
     */
    public NettyServerBootstrap setSpringServletContext(String springServletContext) {
        this.springServletContext = springServletContext;
        return this;
    }

    /**
     * @param serverProperties
     * @author lait.zhang@gmail.com
     * @Date Oct 13, 2016
     * @Description:<code></code>
     */
    public NettyServerBootstrap setServerProperties(String serverProperties) {
        this.serverProperties = serverProperties;
        return this;
    }

    protected boolean getOSMatches(String osNamePrefix) {
        String os = System.getProperty("os.name");
        if (os == null) {
            return false;
        }
        return os.startsWith(osNamePrefix);
    }
}
