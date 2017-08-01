package com.tasfe.framework.netty.servlet.impls;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;

public class ServletInputStreamImpl extends ServletInputStream {

    private HttpRequest request;

    private ByteBufInputStream in;

    public ServletInputStreamImpl(FullHttpRequest request) {
        this.request = request;

        this.in = new ByteBufInputStream(request.content());
    }

    public ServletInputStreamImpl(HttpRequest request) {
        this.request = request;

        this.in = new ByteBufInputStream(Unpooled.buffer(0));
    }

    @Override
    public int read() throws IOException {
        return this.in.read();
    }

    @Override
    public int read(byte[] buf) throws IOException {
        return this.in.read(buf);
    }

    @Override
    public int read(byte[] buf, int offset, int len) throws IOException {
        return this.in.read(buf, offset, len);
    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isReady() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        // TODO Auto-generated method stub

    }

}
