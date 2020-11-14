package com.tonghb.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.awt.*;

/**
 * @author tong
 * @create 2020-11-13-19:13
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {
    // 当通道激活时的操作
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 发送一个Student对象到服务器
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(4).setName("林冲").build();
        // 将消息写入到通道
        ctx.writeAndFlush(student);
    }


    // 读取通道中的内容
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student msg) throws Exception {
        System.out.println("接收到" + ctx.channel().remoteAddress() + "发送过来的信息为：[" + msg.getId() + ", " + msg.getName() + "]");
    }

    // 发生异常时的处理方法
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
        System.out.println("发生了异常：" + cause.getMessage());
    }
}
