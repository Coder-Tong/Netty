package com.tonghb.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author tong
 * @create 2020-11-14-14:10
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    // 读取管道的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        // 读取服务端发送过来的Long类型的数据
        System.out.println("接收到服务端发送过来的消息：" + msg.toString());
    }

    // 重写channelActive发送数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 向客户端发送数据
        for (int i = 0; i < 10; ++i) {
            ByteBuf byteBuf = Unpooled.copiedBuffer("Hello Server[" + i + "]", CharsetUtil.UTF_8);
            // 发送出去
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("发生了异常：" + cause.getMessage());
    }
}
