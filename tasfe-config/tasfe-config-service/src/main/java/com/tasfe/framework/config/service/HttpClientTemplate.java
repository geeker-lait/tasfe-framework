package com.tasfe.framework.config.service;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lait.zhang@gmail.com
 * @Date 20161214
 * @Description:<code>可以替代httpclient的组件</code>
 */
public class HttpClientTemplate extends RestTemplate {
    // 最大链接数
    private int maxTotal = 1000;
    // 默认最大
    private int defaultMaxPerRoute = 1000;
    // 链接存活时间
    private int timeToLive = 30;
    // 重试次数
    private int retryCount = 2;
    // 是否开启请求重试
    private boolean requestSentRetryEnabled = true;
    // 超时
    private int connectTimeout = 5000;

    // 数据读取超时时间，即SocketTimeout
    private int readTimeout = 5000;

    // 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
    private int connectionRequestTimeout = 200;

    private String charset = "UTF-8";
    // 缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存
    private boolean bufferRequestBody = false;

    // 默认转换器
    private List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();

    private static HttpClientTemplate httpClientTemplate = new HttpClientTemplate();

    public static HttpClientTemplate getRestTemplate() {
        return httpClientTemplate;
    }

    private HttpClientTemplate() {
        // 长连接保持30秒
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(timeToLive, TimeUnit.SECONDS);
        // 总连接数
        pollingConnectionManager.setMaxTotal(maxTotal);
        // 同路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        // 重试次数，默认是3次，没有开启
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(retryCount, requestSentRetryEnabled));
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        List<Header> headers = new ArrayList<>();
        // 参考：http://www.cnblogs.com/sink_cup/archive/2011/03/15/http_user_agent.html
        // headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        // 不需要设置，可能会乱码，请参考：http://www.crifan.com/set_accept_encoding_header_to_gzip_deflate_return_messy_code/
        // headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        // headers.add(new BasicHeader("Accept-Language", "zh-CN"));
        headers.add(new BasicHeader("Connection", "Keep-Alive"));
        httpClientBuilder.setDefaultHeaders(headers);
        HttpClient httpClient = httpClientBuilder.build();

        // httpClient连接配置，底层是配置RequestConfig
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // 连接超时
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        // 数据读取超时时间，即SocketTimeout
        clientHttpRequestFactory.setReadTimeout(readTimeout);
        // 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
        clientHttpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        // 缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。
        if (!bufferRequestBody) {
            clientHttpRequestFactory.setBufferRequestBody(false);
        }
        // 添加内容转换器
        // List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new FastJsonHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter(Charset.forName(charset)));
        messageConverters.add(new ByteArrayHttpMessageConverter());
        super.setMessageConverters(messageConverters);
        super.setRequestFactory(clientHttpRequestFactory);
        super.setErrorHandler(new DefaultResponseErrorHandler());
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public boolean isRequestSentRetryEnabled() {
        return requestSentRetryEnabled;
    }

    public void setRequestSentRetryEnabled(boolean requestSentRetryEnabled) {
        this.requestSentRetryEnabled = requestSentRetryEnabled;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isBufferRequestBody() {
        return bufferRequestBody;
    }

    public void setBufferRequestBody(boolean bufferRequestBody) {
        this.bufferRequestBody = bufferRequestBody;
    }
}
