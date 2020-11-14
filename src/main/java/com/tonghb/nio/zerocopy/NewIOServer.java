package com.tonghb.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author tong
 * @create 2020-11-10-15:24
 */
public class NewIOServer {
    public static void main(String[] args) throws IOException {
        // 创建一个Server服务器端
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 为该serverSocketChannel绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 创建一个Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 4);

        // 循环监听
        while (true) {
            // 等待客户端连接
            SocketChannel socketChannel = serverSocketChannel.accept();

            // 获取客户端发送过来的数据
            int count = 0;
            try {
                while ((count = socketChannel.read(buffer)) != -1) {
                    // 将buffer倒带, position = 0, mark = -1
                    buffer.rewind();
                }
            } catch (IOException e) {
                break;
            }
        }
    }
}
