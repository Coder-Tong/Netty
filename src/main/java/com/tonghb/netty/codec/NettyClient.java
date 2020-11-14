package com.tonghb.netty.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * @author tong
 * @create 2020-11-13-19:05
 */
public class NettyClient {
    public static void main(String[] args) {
        // 创建一个线程组
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 为该线程组创建一个启动对象
        Bootstrap bootstrap = new Bootstrap();

        // 为该bootStrap设置参数
        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 得到pipeLine
                            ChannelPipeline pipeline = ch.pipeline();

                            // 在pipeline设置protoEncoder编码器
                            pipeline.addLast("protobuf encoder", new ProtobufEncoder());

                            // 在pipeline设置protoDecoder解码器
                            pipeline.addLast("protobuf decoder", new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));

                            // 为pipeline设置处理器
                            pipeline.addLast(new NettyClientHandler());

                        }
                    });

            // 连接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8888).sync();

            // 异步关闭通道监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅的关闭线程组
            eventLoopGroup.shutdownGracefully();
        }
    }
}
