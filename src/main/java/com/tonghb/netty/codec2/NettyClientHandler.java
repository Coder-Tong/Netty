package com.tonghb.netty.codec2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

/**
 * @author tong
 * @create 2020-11-13-19:13
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    // 当通道激活时的操作
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 随机发送Student 或者 Worker对象
        int random = new Random().nextInt(3);
        MyDataInfo.MyMessage msg = null;

        if (random == 0) {   // 发送一个Student类对象
            msg = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.StudentType)
                    .setStudent(MyDataInfo.Student.newBuilder().setId(5).setName("卢俊义").build()).build();
        } else {
            msg = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                    .setWorker(MyDataInfo.Worker.newBuilder().setName("小李").setAge(32).build()).build();
        }
        // 将消息写入到通道
        ctx.writeAndFlush(msg);
    }


    // 读取通道中的内容
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        // 接收服务端发送过来的消息
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        // 根据对象的类型获取数据
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("接收到服务端发送过来的对象：[" + student.getId() + ", " + student.getName() + "]");
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("接收到服务端发送过来的对象：[" + worker.getName() + ", " + worker.getAge() + "]");
        } else {
            System.out.println("没有接收到合法的消息");
        }
    }

    // 发生异常时的处理方法
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
        System.out.println("发生了异常：" + cause.getMessage());
    }
}
