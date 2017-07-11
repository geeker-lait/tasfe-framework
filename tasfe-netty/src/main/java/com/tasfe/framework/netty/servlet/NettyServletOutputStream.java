package com.tasfe.framework.netty.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import java.io.IOException;
import java.io.StringWriter;

public class NettyServletOutputStream extends ServletOutputStream {

    private StringWriter stringWriter;

    public NettyServletOutputStream() {
        this.stringWriter = new StringWriter();
    }

    @Override
    public void write(int b) throws IOException {
        stringWriter.write(b);
    }

    public StringWriter getStringWriter() {
        return stringWriter;
    }

    @Override
    public boolean isReady() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        // TODO Auto-generated method stub

    }
}
