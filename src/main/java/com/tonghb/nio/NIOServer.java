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
 * @create 2020-11-08-21:10
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个Selector对象
        Selector selector = Selector.open();

        // 绑定一个端口6666
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 将ServerSocketChannel注册到Selector上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            if (selector.select(1000) == 0) {  // 没有事件发生
                System.out.println("服务器等了1s, 无连接");
                continue;
            }

            // 有连接事件，获取到相关的Selection集合
            // 1. 如果返回的大于0，表示已经获取到了关注的事件
            // 2. 关注事件的集合 selector.selectedKeys()
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历集合，取出通道
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 获取到SelectionKey
                SelectionKey key = keyIterator.next();

                // 根据key对应的通道发生的事件做相应的处理
                if (key.isAcceptable()) {   // 有新的客户端连接过来
                    // 为该客户端生成一个SocketChannel
                    SocketChannel accept = serverSocketChannel.accept();

                    // 将该通道设置为非阻塞
                    accept.configureBlocking(false);

                    // 将该客户端注册进入到selector中，为通道绑定一个Buffer
                    accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                // 如果是读事件
                if (key.isReadable()) {
                    // 通过key获取对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();

                    // 获取该channel关联的buffer
                    ByteBuffer attachment = (ByteBuffer) key.attachment();
                    channel.read(attachment);

                    System.out.println("From 客户端 " + new String(attachment.array()));
                }

                // 从集合中将key删除，避免重复消费
                keyIterator.remove();
            }
        }
    }
}
