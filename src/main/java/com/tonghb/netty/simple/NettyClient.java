package com.tonghb.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author tong
 * @create 2020-11-05-17:05
 */
public class NettyClient {
    public static void main(String[] args) {
        // 创建一个事件循环组
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();

        try {
            // 创建客户端启动对象
            // 注意客户端使用的不是serverBootStrap 而是 Bootstrap
            Bootstrap bootstrap = new Bootstrap();

            // 设置相关参数
            bootstrap.group(loopGroup)   // 设置工作线程组
                     .channel(NioSocketChannel.class)   // 设置客户端通道的实现类
                     .handler(new ChannelInitializer<SocketChannel>() {    // 设置客户端的处理器
                         @Override
                         protected void initChannel(SocketChannel socketChannel) throws Exception {
                             socketChannel.pipeline().addLast(new NettyClientHandler());
                         }
                     });

            System.out.println("客户端启动成功");

            // 启动客户端去连接服务器
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭循环组
            loopGroup.shutdownGracefully();
        }


    }
}
