package com.tonghb.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @author tong
 * @create 2020-11-13-15:37
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    // 建立连接
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("HandlerAdded 被调用 " + ctx.channel().id().asLongText());
        System.out.println(ctx.channel().remoteAddress() + "建立连接");
    }

    // 关闭连接
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("HandlerRemoved 被调用 " + ctx.channel().id().asLongText());
        System.out.println(ctx.channel().remoteAddress() + "断开连接");
    }

    // 读取通道的内容
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息: " + msg.text());

        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间 [" + LocalDateTime.now() + "] " + msg.text()));
    }

    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
        System.out.println("异常发生：" + cause.getMessage());
    }
}
