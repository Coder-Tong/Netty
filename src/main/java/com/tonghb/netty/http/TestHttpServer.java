package com.tonghb.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author tong
 * @create 2020-11-11-20:21
 */
public class TestHttpServer {
    public static void main(String[] args) {
        // 创建两个线程池
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建一个启动器对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            // 使用链式编程创建对应的参数
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new TestHttpServerInitializer());

            // 为ServerSocketChannel绑定对应的端口
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            // 设置异步的关闭事件
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅的关闭创建的线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
