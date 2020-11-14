package com.tonghb.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author tong
 * @create 2020-11-10-13:43
 */
public class Server {
    // 定义相关属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    // 构造器，初始化相关变量
    public Server() {
        try {
            // 初始化选择器
            selector = Selector.open();
            // listenChannel
            listenChannel = ServerSocketChannel.open();
            // 为listenChannel绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 将listenChannel设置为非阻塞
            listenChannel.configureBlocking(false);
            // 将listenChannel注册到Selector中，关心的事件为OP_ACCEPT
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听事件
    public void listen() {
        while (true) {
            try {
                // 阻塞的方法
                int count = selector.select();
                // 当count > 0, 表明有事件发生
                if (count > 0) {
                    // 得到所有的事件
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    // 处理事件
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        // 连接事件
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            // 将该socketChannel设置为非阻塞
                            socketChannel.configureBlocking(false);
                            // 注册进selector
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            // 提示
                            System.out.println(socketChannel.getRemoteAddress() + "上线");
                        }

                        // 通道是可读状态
                        if (key.isReadable()) {
                            readData(key);
                        }

                        // 将当前的key删除
                        iterator.remove();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 读取客户端的消息
    private void readData(SelectionKey key) {
        // 定义一个SocketChannel
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            // 得到缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 从通道中读取数据到buffer中
            int count = channel.read(buffer);

            // 根据count的值做处理
            if (count > 0) {
                // 把缓冲区的数据转为字符串
                String msg = new String(buffer.array());
                System.out.println("From 客户端：" + msg);

                // 向其他的客户端转发消息
                sendInfoToOtherClient(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了");
                // 取消注册
                key.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // 向其他的客户转发消息
    private void sendInfoToOtherClient(String msg, SocketChannel channel) throws IOException {
        // 服务器转发消息
        System.out.println("服务器开始转发消息");
        for (SelectionKey key : selector.keys()) {
            // 通过key取出socketChannel
            SelectableChannel targetChannel = key.channel();

            // 排除自己
            if (targetChannel instanceof  SocketChannel && targetChannel != channel) {
                SocketChannel target = (SocketChannel) targetChannel;
                // 将msg存储到buffer中
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer中的数据写入到通道中
                target.write(buffer);
            }
        }
    }


    public static void main(String[] args) {
        // 创建一个服务器对象
        Server server = new Server();

        // 监听
        server.listen();
    }
}
