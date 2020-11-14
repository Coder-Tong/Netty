package com.tonghb.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author tong
 * @create 2020-11-10-14:17
 */
public class Client {
    // 定义相关属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    // 初始化相关操作
    public Client() {
        try {
            selector = Selector.open();
            // 得到channel
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            // 将Channel设置为非阻塞
            socketChannel.configureBlocking(false);
            // 将channel注册进Selector
            socketChannel.register(selector, SelectionKey.OP_READ);
            // 得到username
            username = socketChannel.getLocalAddress().toString().substring(1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 向服务器发送消息
    public void sendInfo(String msg) {
        msg = username + " say: " + msg;

        try {
            // 将Buffer中的数据写入到通道中
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从服务器读取消息
    public void readInfo() {
        try {
            int count = selector.select();

            // 有事件发生
            if (count > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    // 读事件发生
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        System.out.println(new String(buffer.array()));
                    }

                    // 移除key
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 启动客户端
        Client client = new Client();

        // 启动一个线程
        new Thread(() -> {
            while (true) {
                client.readInfo();
                // 每隔3秒读取一次数据
                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 数据发送给服务端
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String str = sc.nextLine();
            client.sendInfo(str);
        }
    }
}
