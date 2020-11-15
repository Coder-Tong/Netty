package com.tonghb.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author tong
 * @create 2020-11-14-14:05
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private static int count = 0;

    // 读取通道内的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println("读取到客户端发送过来的消息：[长度：" + msg.getLen() + ", " +
                "内容为：" + new String(msg.getContent(), CharsetUtil.UTF_8) + "]");
        System.out.println("读取客户端发送的数据：" + (++this.count));

        // 向客户端回送数据
        String res = "Hello Client";
        // 转换为byte数组
        byte[] content = res.getBytes();
        int len = content.length;
        // 创建一个MessageProtocol对象
        ctx.writeAndFlush(new MessageProtocol(len, content));

    }


    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("发生异常：" + cause.getMessage());
    }
}
