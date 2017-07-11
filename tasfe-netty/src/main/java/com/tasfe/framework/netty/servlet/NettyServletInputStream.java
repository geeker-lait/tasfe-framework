package com.tasfe.framework.netty.servlet;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class NettyServletInputStream extends ServletInputStream {

    private ByteBuffer buffer;

    public NettyServletInputStream(byte[] content) {
        buffer = ByteBuffer.allocate(content.length);
        buffer.put(content);
        buffer.rewind();
    }

    @Override
    public int read() throws IOException {
        try {
            return buffer.get();
        } catch (BufferUnderflowException be) {
            return -1;
        }
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
