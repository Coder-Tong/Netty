package com.tonghb.netty.protocoltcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author tong
 * @create 2020-11-14-13:45
 */
public class NettyClient {
    public static void main(String[] args) {
        // 创建一个线程组
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 创建一个启动对象
        Bootstrap bootstrap = new Bootstrap();

        // 设置相关参数
        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 得到一个pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            // 设置MessageProtocol编解码器
                            pipeline.addLast(new MyMessageEncoder());
                            pipeline.addLast(new MyMessageDecoder());

                            // 设置自定义的Handler
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });

            // 连接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8888).sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
