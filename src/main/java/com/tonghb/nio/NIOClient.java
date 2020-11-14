package com.tonghb.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author tong
 * @create 2020-11-08-21:46
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        // 得到一个SocketChannel
        SocketChannel socketChannel = SocketChannel.open();

        // 将该端口设置为非阻塞
        socketChannel.configureBlocking(false);

        // 为该socketChannel设置服务器的IP地址和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        // 连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他的事情");
            }
        }

        // 连接成功
        String str = "Hello, Java";

        // 将字节数组放入到Buffer中去
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        // 发送数据，将Buffer数据写入Channel
        socketChannel.write(buffer);

        System.in.read();
    }
}
