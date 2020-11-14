package com.tonghb.netty.inandoutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author tong
 * @create 2020-11-14-14:05
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<Long> {
    // 读取通道内的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("读取到客户端发送过来的消息：" + msg);

        // 向客户端回送数据
        ctx.writeAndFlush(78910L);
    }


    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("发生异常：" + cause.getMessage());
    }
}
