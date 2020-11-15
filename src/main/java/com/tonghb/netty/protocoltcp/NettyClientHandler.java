package com.tonghb.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author tong
 * @create 2020-11-14-14:10
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    // 读取管道的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 读取服务端发送过来的Long类型的数据
        System.out.println("接收到服务端发送过来的消息：[长度：" + msg.getLen() + ", 内容："
                + new String(msg.getContent()) + "]");
    }

    // 重写channelActive发送数据
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 向服务端发送数据
        for (int i = 0; i < 10; ++i) {
            String msg = "Hello Server[" + i + "]";
            // 转换为字节数组
            byte[] content = msg.getBytes();
            int len = content.length;

            // 发送出去
            ctx.writeAndFlush(new MessageProtocol(len, content));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("发生了异常：" + cause.getMessage());
    }
}
