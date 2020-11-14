package com.tonghb.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author tong
 * @create 2020-11-08-21:57
 */
public class NIOClientTest {
    public static void main(String[] args) throws IOException {
        // 创建SocketChannel
        SocketChannel socketChannel = SocketChannel.open();

        // 将该socketChannel设置为非阻塞
        socketChannel.configureBlocking(false);

        // 连接服务器
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        if (!socketChannel.connect(inetSocketAddress)) {
            // 没有连接上去
            while (!socketChannel.finishConnect()) {
                System.out.println("客户端正在连接服务器...");
            }
        }

        // 连接成功，向channel中写入内容
        String str = "Hello World";
        socketChannel.write(ByteBuffer.wrap(str.getBytes()));

        // 阻塞
        System.in.read();

    }
}
