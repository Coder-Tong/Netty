package com.tonghb.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author tong
 * @create 2020-11-13-18:51
 */
public class NettyServer {
    public static void main(String[] args) {
        // 创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建一个启动类对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            // 设置启动对象的相关参数
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 得到管道
                            ChannelPipeline pipeline = ch.pipeline();

                            // 加入protobuf decoder，指定对哪种对象进行解码
                            pipeline.addLast("protobuf decoder", new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));

                            // 在pipeline设置protoEncoder编码器
                            pipeline.addLast("protobuf encoder", new ProtobufEncoder());

                            // 设置处理器
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });

            // 绑定监听的端口
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();

            // 异步关闭监听端口
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅的关闭线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
