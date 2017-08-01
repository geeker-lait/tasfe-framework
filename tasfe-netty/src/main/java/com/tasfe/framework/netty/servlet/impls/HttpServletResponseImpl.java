package com.tasfe.framework.netty.servlet.impls;

import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.tasfe.framework.netty.servlet.exception.NettyServletRuntimeException;

import io.netty.handler.codec.http.*;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.util.WebUtils;

public class HttpServletResponseImpl implements HttpServletResponse {

    private static final String CHARSET_PREFIX = "charset=";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private String characterEncoding = WebUtils.DEFAULT_CHARACTER_ENCODING;
    private boolean charset = false;
    private String contentType;
    private final Map<String, HeaderValueHolder> headers = new LinkedCaseInsensitiveMap<HeaderValueHolder>();

    private HttpResponse originalResponse;
    private ServletOutputStreamImpl outputStream;
    private PrintWriterImpl writer;
    private boolean responseCommited = false;
    private Locale locale = null;

    public HttpServletResponseImpl(FullHttpResponse response) {
        this.originalResponse = response;
        this.outputStream = new ServletOutputStreamImpl(response);
        this.writer = new PrintWriterImpl(this.outputStream);
    }

    public HttpResponse getOriginalResponse() {
        return originalResponse;
    }

    @Override
    public void addCookie(Cookie cookie) {
        String result = ServerCookieEncoder
                .encode(new io.netty.handler.codec.http.DefaultCookie(cookie.getName(), cookie.getValue()));
        this.originalResponse.headers().set(SET_COOKIE, result);
    }

    @Override
    public void addDateHeader(String name, long date) {
        HttpHeaders.addHeader(this.originalResponse, name, date);
    }

    @Override
    public void addHeader(String name, String value) {
        HttpHeaders.addHeader(this.originalResponse, name, value);
    }

    @Override
    public void addIntHeader(String name, int value) {
        HttpHeaders.addIntHeader(this.originalResponse, name, value);
    }

    @Override
    public boolean containsHeader(String name) {
        return this.originalResponse.headers().contains(name);
    }

    @Override
    public void sendError(int sc) throws IOException {
        this.originalResponse.setStatus(HttpResponseStatus.valueOf(sc));
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        // Fix the following exception
        /*
		 * java.lang.IllegalArgumentException: reasonPhrase contains one of the
		 * following prohibited characters: \r\n: FAILED - Cannot find View Map
		 * for null.
		 * 
		 * at io.netty.handler.codec.http.HttpResponseStatus.<init>(
		 * HttpResponseStatus.java:514) ~[netty-all-4.1.0.Beta3.jar:4.1.0.Beta3]
		 * at io.netty.handler.codec.http.HttpResponseStatus.<init>(
		 * HttpResponseStatus.java:496) ~[netty-all-4.1.0.Beta3.jar:4.1.0.Beta3]
		 */
        if (msg != null) {
            msg = msg.replace('\r', ' ');
            msg = msg.replace('\n', ' ');
        }
        this.originalResponse.setStatus(new HttpResponseStatus(sc, msg));
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        setStatus(SC_FOUND);
        setHeader(LOCATION, location);
    }

    @Override
    public void setDateHeader(String name, long date) {
        HttpHeaders.setHeader(this.originalResponse, name, date);
    }

    @Override
    public void setHeader(String name, String value) {
        HttpHeaders.setHeader(this.originalResponse, name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        originalResponse.headers().set(name, value);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return this.writer;
    }

    @Override
    public void setStatus(int sc) {
        this.originalResponse.setStatus(HttpResponseStatus.valueOf(sc));
    }

    @Override
    public void setStatus(int sc, String sm) {
        this.originalResponse.setStatus(new HttpResponseStatus(sc, sm));
    }

    @Override
    public String getContentType() {
        return this.originalResponse.headers().get(HttpHeaderNames.CONTENT_TYPE);
    }

    @Override
    public void setContentType(String type) {
        this.originalResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, type);
    }

    @Override
    public void setContentLength(int len) {
        this.originalResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, len);
    }

    @Override
    public boolean isCommitted() {
        return this.responseCommited;
    }

    @Override
    public void reset() {
        if (isCommitted())
            throw new IllegalStateException("Response already commited!");
        this.originalResponse.headers().clear();
        this.resetBuffer();
    }

    @Override
    public void resetBuffer() {
        if (isCommitted())
            throw new IllegalStateException("Response already commited!");

        this.outputStream.resetBuffer();
    }

    @Override
    public void flushBuffer() throws IOException {
        this.getWriter().flush();
        this.responseCommited = true;
    }

    @Override
    public int getBufferSize() {
        return this.outputStream.getBufferSize();
    }

    @Override
    public void setBufferSize(int size) {
        // we using always dynamic buffer for now
    }

    @Override
    public String encodeRedirectURL(String url) {
        return this.encodeURL(url);
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return this.encodeURL(url);
    }

    @Override
    public String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new NettyServletRuntimeException("Error encoding url!", e);
        }
    }

    @Override
    public String encodeUrl(String url) {
        return this.encodeRedirectURL(url);
    }
	/*
	@Override
	public String getCharacterEncoding() {
		return HttpHeaders.getHeader(this.originalResponse, HttpHeaderNames.CONTENT_ENCODING);
	}

	@Override
	public void setCharacterEncoding(String charset) {
		HttpHeaders.setHeader(this.originalResponse, HttpHeaderNames.CONTENT_ENCODING, charset);
	}*/


    @Override
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
        this.charset = true;
        updateContentTypeHeader();
        this.originalResponse.headers().set(HttpHeaderNames.CONTENT_ENCODING, characterEncoding);
    }


    @Override
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }


    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale loc) {
        this.locale = loc;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public void setContentLengthLong(long len) {
        // TODO Auto-generated method stub

    }

    private void updateContentTypeHeader() {
        if (this.contentType != null) {
            StringBuilder sb = new StringBuilder(this.contentType);
            if (!this.contentType.toLowerCase().contains(CHARSET_PREFIX) && this.charset) {
                sb.append(";").append(CHARSET_PREFIX).append(this.characterEncoding);
            }
            doAddHeaderValue(CONTENT_TYPE_HEADER, sb.toString(), true);
        }
    }

    private void doAddHeaderValue(String name, Object value, boolean replace) {
        HeaderValueHolder header = HeaderValueHolder.getByName(this.headers, name);
        Assert.notNull(value, "Header value must not be null");
        if (header == null) {
            header = new HeaderValueHolder();
            this.headers.put(name, header);
        }
        if (replace) {
            header.setValue(value);
        } else {
            header.addValue(value);
        }
    }

}