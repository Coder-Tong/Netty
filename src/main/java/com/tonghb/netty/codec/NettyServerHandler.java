package com.tonghb.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author tong
 * @create 2020-11-13-18:59
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {
    // 读取通道中的数据
    @Override
    public void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student msg) throws Exception {
        // 读取客户端发送过来的StudentPOJO.Student类型
        System.out.println("接收到 " + ctx.channel().remoteAddress() + " 发送过来的消息：[" + msg.getId() + ", " + msg.getName() + "]");
    }

    // 通道中的数据读取完毕之后，回显给客户端
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 服务端响应一个StudentPOJO.Student对象
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(1).setName("宋江").build();
        ctx.writeAndFlush(student);
    }

    // 发生异常时关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
        System.out.println("发生异常: " + cause.getMessage());
    }
}
