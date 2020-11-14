package com.tonghb.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author tong
 * @create 2020-11-13-15:08
 */
public class WebSocketServer {
    public static void main(String[] args) {
        // 创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建一个启动器对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            // 配置启动对象的相关参数
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 得到pipeLine
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            // 设置处理器
                            // 1.设置http协议的编解码器
                            pipeline.addLast("httpServerCodec", new HttpServerCodec());

                            // 2.由于是基于http协议的，所以是以块的方式处理的，添加chunkedWrite处理器
                            pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());

                            /**
                             * 说明
                             * 1. 因为Http协议在传输过程中是分段的，HttpObjectAggregator 就是可以将多个段聚合起来
                             * 2. 这就是为什么当浏览器发送大量数据时，就会发出多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /**
                             * 说明
                             * 1. 对于webSocket数据是帧的形式传递
                             * 2. 可以看到WebSocketFrame 下面有六个子类
                             * 3. 浏览器请求时，ws://localhost:7000/xxx  xxx表示请求的url
                             * 4. 核心功能是将http协议升级为ws协议，保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            // 自定义的handler, 处理业务逻辑
                            pipeline.addLast(new WebSocketServerHandler());

                        }
                    });

            // 绑定监听的端口
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();

            // 关闭异步监听端口
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅的关闭通道
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
