package com.tonghb.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author tong
 * @create 2020-11-12-19:49
 */
public class GroupChatServer {
    // 定义相关属性
    private int port;

    // 初始化相关方法
    public GroupChatServer(int port) {
        this.port = port;
    }

    // 处理客户端请求
    public void run() {

            NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();

            // 创建一个启动器对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            // 设置相关参数
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 获取到pipeLine
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            // 添加处理器
                            // 向pipeline中加入解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());

                            // 自己的处理器
                            pipeline.addLast("myHandler", new GroupChatServerHandler());
                        }
                    });

            System.out.println("服务端启动");

            // 绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            // 关闭监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭工作组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    // 主程序
    public static void main(String[] args) {
        // 启动服务端
        new GroupChatServer(8888).run();
    }
}
