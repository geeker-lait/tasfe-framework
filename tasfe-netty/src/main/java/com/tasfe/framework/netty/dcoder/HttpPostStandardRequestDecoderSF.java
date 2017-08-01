package com.tasfe.framework.netty.dcoder;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.handler.codec.http.multipart.HttpPostStandardRequestDecoder;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static io.netty.buffer.Unpooled.EMPTY_BUFFER;
import static io.netty.buffer.Unpooled.buffer;

/**
 * http post only application/json,no need decode attributions
 * created by lait.zhang at 2017/6/27
 */
@Slf4j
public class HttpPostStandardRequestDecoderSF extends HttpPostStandardRequestDecoder {

    private HttpDataFactory factory;
    private HttpRequest request;
    private Charset charset;

    public HttpPostStandardRequestDecoderSF(HttpRequest request) {
        super(request);
    }

    public HttpPostStandardRequestDecoderSF(HttpDataFactory factory, HttpRequest request) {
        super(factory, request);
    }

    public HttpPostStandardRequestDecoderSF(HttpDataFactory factory, HttpRequest request, Charset charset) {
        super(factory, request, charset);
    }

    /**
     * http post is json,no parse attribute
     *
     * @param content
     * @return
     */
    public HttpPostStandardRequestDecoder offer(HttpContent content) {
//		decoder.checkDestroyed();

        // Maybe we should better not copy here for performance reasons but this will need
        // more care by the caller to release the content in a correct manner later
        // So maybe something to optimize on a later stage
        ByteBuf buf = content.content();
        /*if (undecodedChunk == null) {
			undecodedChunk = buf.copy();
		} else {
			undecodedChunk.writeBytes(buf);
		}
		if (content instanceof LastHttpContent) {
			isLastChunk = true;
		}
//		parseBody();
		if (undecodedChunk != null && undecodedChunk.writerIndex() > discardThreshold) {
			undecodedChunk.discardReadBytes();
		}*/
        return this;
    }


}

