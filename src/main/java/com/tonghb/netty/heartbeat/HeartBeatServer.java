package com.tonghb.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author tong
 * @create 2020-11-12-21:07
 */
public class HeartBeatServer {
    public static void main(String[] args) {
        // 创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建一个启动对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //配置启动器
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 得到pipeLine
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 加入一个netty 提供得 IdlestHandler
                            /**
                             * IdleStateHandler 是 netty提供的处理空闲状态的处理器
                             * 说明：
                             * 1. readerIdleTime: 表示多长时间没有读取数据，就会发送一个心跳检测包
                             * 2. writeIdleTime: 表示多长时间没有写数据，就会发送一个心跳检测包
                             * 3. allIdleTime: 表示多长时间没有读写, 就会发送一个心跳检测包
                             * 4. 当 IdleStateEvent 触发后，就会传递给管道的下一个handler去处理
                             * 通过调用下一个handler的userEventTrigger，在该方法中去处理IdleStateEvent(读空闲，写空闲，读写空闲)
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的Handler(自定义)
                            pipeline.addLast(new HeartBeatServerHandler());
                        }
                    })
                    .handler(new LoggingHandler(LogLevel.INFO));

            // 得到channelFuture对象
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
