package com.tasfe.framework.netty.servlet.impls;

import java.io.OutputStream;
import java.io.PrintWriter;

public class PrintWriterImpl extends PrintWriter {

    private boolean flushed = false;

    public PrintWriterImpl(OutputStream out) {
        super(out);
    }

    @Override
    public void flush() {
        super.flush();
        this.flushed = true;
    }

    public boolean isFlushed() {
        return flushed;
    }
}
