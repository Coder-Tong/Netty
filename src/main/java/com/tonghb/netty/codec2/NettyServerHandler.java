package com.tonghb.netty.codec2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

/**
 * @author tong
 * @create 2020-11-13-18:59
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    // 读取通道中的数据
    @Override
    public void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        // 读取客户端发送过来的MyDataInfo.MyMessage类型的对象
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            // 得到学生类对象
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("接收到客户端发送过来的消息：[" + student.getId() + ", " + student.getName() + "]");
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType){
            // 得到客户端发送过来的对象
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("接收到客户端发送过来的消息：[" + worker.getName() + ", " + worker.getAge() + "]");
        } else {
            System.out.println("客户端发送的消息格式不合法");
        }
    }

    // 通道中的数据读取完毕之后，回显给客户端
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 服务端对象向客户端回显一个MyMessage对象
        int random = new Random().nextInt(3);

        MyDataInfo.MyMessage msg = null;
        if (random == 0) {
            msg = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                    .setStudent(MyDataInfo.Student.newBuilder().setId(6).setName("李逵").build()).build();
        } else {
            msg = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                    .setWorker(MyDataInfo.Worker.newBuilder().setName("石阡").setAge(22).build()).build();
        }

        ctx.writeAndFlush(msg);
    }

    // 发生异常时关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
        System.out.println("发生异常: " + cause.getMessage());
    }
}
