package com.tonghb.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author tong
 * @create 2020-11-08-21:30
 */
public class NIOServerTest {
    public static void main(String[] args) throws IOException {
        // 首先创建一个ServerSocketChannel，类似于ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 然后创建一个Selector
        Selector selector = Selector.open();

        // 为socketChannel绑定端口号
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 将socketChannel设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 将ServerSocketChannel绑定到selector上,serverSocketChannel关注的事件为连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环的监听客户端的连接请求
        while (true) {
            // 调用Select(ms)方法，获取是否有连接请求
            if (selector.select(1000) == 0) {   // 没有连接请求
                System.out.println("服务端等了1s, 没有连接请求");
                continue;
            }

            // 有连接请求，获取selectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历集合
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 得到key
                SelectionKey key = keyIterator.next();

                // 根据Key的类型不同作相应的处理
                if (key.isAcceptable()) {  // 连接请求
                    // 创建一个连接通道
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    // 将该通道设置为非阻塞状态
                    socketChannel.configureBlocking(false);

                    // 将该通道注册到selector中，并为该通道绑定一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                // 如果是一个读时间
                if (key.isReadable()) {
                    // 先获取对应的通道
                    SocketChannel channel = (SocketChannel) key.channel();

                    // 然后获取对应的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();

                    // 将Buffer中的内容写入到channel中
                    channel.read(buffer);

                    // 输出结果
                    System.out.println("From 客户端：" + new String(buffer.array()));
                }

                // 处理请求之后，将key移除，避免重复处理
                keyIterator.remove();
            }
        }
    }
}
