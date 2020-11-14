package com.tonghb.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author tong
 * @create 2020-11-11-20:21
 */

/**
 * 说明：
 * 1. SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter
 * 2. HttpObject 客户端和服务器端相互通讯的数据被封装成了 HttpObject
 */

public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    // 读取客户端的数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        // 判断 msg 是不是 HttpRequest 请求
        if (httpObject instanceof HttpRequest) {
            System.out.println("httpObject类型: " + httpObject.getClass());
            System.out.println("客户端地址：" + channelHandlerContext.channel().remoteAddress());

            // 对于获取到的请求进行强制类型转换
            HttpRequest request = (HttpRequest) httpObject;
            // 获取请求的uri
            URI uri = new URI(request.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求为 favicon.ico, 不处理该请求！");
                return;
            }

            // 回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("Hello, I'm Server", CharsetUtil.UTF_8);

            // 构造一个Http的响应，即httpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            // 设置响应的头信息
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            // 将构建好的response返回
            channelHandlerContext.writeAndFlush(response);
        }
    }
}
