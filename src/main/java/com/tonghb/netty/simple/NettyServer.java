package com.tonghb.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author tong
 * @create 2020-11-05-16:19
 */
public class NettyServer {
    public static void main(String[] args) {
        // 创建BossGroup 和 WorkerGroup
        /**
         * 说明：
         * 1. BossGroup只处理连接请求，真正处理客户端请求的是workerGroup
         * 2. 两个都是无限循环
         * 3. bossGroup 和 WorkerGroup 含有子线程（NioEventLoop）的个数
         *    默认：实际CPU 的核数 * 2
         */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 使用链式编程来设置
            serverBootstrap.group(bossGroup, workerGroup)  // 设置两个线程组
                           .channel(NioServerSocketChannel.class)   // 使用NioServerSocketChannel作为服务器的通道实现
                           .option(ChannelOption.SO_BACKLOG, 128)    // 设置线程队列的连接个数
                           .childOption(ChannelOption.SO_KEEPALIVE, true)  // 保持活动连接状态
                           .childHandler(new ChannelInitializer<SocketChannel>() {  // 创建一个通道测试对象
                               // 给pipeLine设置处理器
                               @Override
                               protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    socketChannel.pipeline().addLast(new NettyServerHandler());
                               }
                           });   // 给workerGroup对应的管道设置处理器

            System.out.println("......服务器 is ready...");

            // 绑定一个端口并且同步，生成一个ChannelFuture对象
            // 启动服务器（并绑定端口）
            ChannelFuture future = serverBootstrap.bind(6668).sync();

            // 给future对象添加Listener，来监听异步执行的结果
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("绑定端口成功");
                    } else {
                        System.out.println("绑定端口失败");
                    }
                }
            });

            // 对关闭通道进行监听
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
