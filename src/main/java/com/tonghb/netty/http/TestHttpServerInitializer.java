package com.tonghb.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author tong
 * @create 2020-11-11-20:22
 */
public class TestHttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 向管道加入处理器
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 1. 加入一个netty提供的httpServerCodec   codec => (coder -> deCoder)
        pipeline.addLast("myHttpServerCodec", new HttpServerCodec());

        // 2. 增加自定义的处理器
        pipeline.addLast("myTestHttpServerHandler", new TestHttpServerHandler());
    }
}
