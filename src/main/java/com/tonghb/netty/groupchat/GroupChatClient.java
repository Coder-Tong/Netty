package com.tonghb.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author tong
 * @create 2020-11-12-20:41
 */
public class GroupChatClient {
    // 相关属性
    private String host;
    private int port;

    // 构造函数
    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // 启动客户端
    public void run() {
        // 创建一个NioEventLoopGroup
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 创建一个bootstrap
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 得到pipeLine
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            // 添加编码器
                            pipeline.addLast("decoder", new StringDecoder());
                            // 添加解码器
                            pipeline.addLast("encoder", new StringEncoder());

                            // 添加自定义的handler
                            pipeline.addLast("myClientHandler", new GroupChatClientHandler());
                        }
                    });

            // 绑定端口
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            Channel channel = channelFuture.channel();
            System.out.println("------" + channel.localAddress() + "-----");
            // 客户端需要输入信息，创建一个扫描器
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()) {
                String msg = sc.nextLine();
                // 通过channel发送到服务器
                channel.writeAndFlush(msg);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅的关闭
            eventLoopGroup.shutdownGracefully();
        }
    }

    // 主方法
    public static void main(String[] args) {
        new GroupChatClient("localhost", 8888).run();
    }
}
