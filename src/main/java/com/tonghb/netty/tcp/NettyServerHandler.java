package com.tonghb.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author tong
 * @create 2020-11-14-14:05
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static int count = 0;

    // 读取通道内的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);

        // 将 buffer转成字符串
        String message = new String(bytes, Charset.forName("UTF-8"));
        System.out.println("读取到客户端发送过来的消息：" + message);
        System.out.println("读取客户端发送的数据：" + (++this.count));
    }


    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("发生异常：" + cause.getMessage());
    }
}
