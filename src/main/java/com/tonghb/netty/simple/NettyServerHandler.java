package com.tonghb.netty.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author tong
 * @create 2020-11-05-16:45
 */

/**
 * 说明：
 * 1. 自定义的一个Handler 需要继承 netty 规定好的某个HandlerAdapter
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    // 读取客户端发送的消息

    /**
     * @param ctx 上下文信息，含有管道pipeLine，通道 Channel, 地址
     * @param msg 客户端发送的数据，默认是 Object 的形式
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /**
         * 模拟非常耗时的操作
         */
//        Thread.sleep(10000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, client~", CharsetUtil.UTF_8));

        /**
         * 解决方案1，将耗时长的任务加入到taskQueue中
         */
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, client~", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, client~~", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        // 用户自定义的定时任务，会放到 ScheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, client~~~", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);


        System.out.println("go on ......");
//        System.out.println("服务器读取线程信息：" + Thread.currentThread().getName());
//        System.out.println("Server cxt" + ctx);
//
//        // 将msg转成一个ByteBuf
//        // ByteBuf 是 Netty 自定义的
//        ByteBuf buf = (ByteBuf) msg;
//
//        System.out.println("客户端发送的消息是：" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址是：" + ctx.channel().remoteAddress());
    }


    // 数据读取完毕，将数据写入通道
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据写入缓存并刷新
        // 一般来讲，对发送的数据进行编码，用于回复客户端
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello Client", CharsetUtil.UTF_8));
    }

    // 处理异常，一般是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
