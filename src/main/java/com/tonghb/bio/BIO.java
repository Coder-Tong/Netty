package com.tonghb.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tong
 * @create 2020-11-04-9:52
 */
public class BIO {
    public static void main(String[] args) throws IOException {
        // 创建一个线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();

        // 创建一个ServerSocket,监听的端口为6666
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("启动服务器");

        // 循环的监听数据
        while (true) {
            // 等待客户端的连接
            final Socket accept = serverSocket.accept();
            System.out.println("连接到一个客户端");
            // 处理客户端的请求
            threadPool.execute(() -> handler(accept));
        }
    }


    // 处理请求的方法
    private static void handler(Socket socket) {
        // 打印线程的信息
        System.out.println(Thread.currentThread().getId() + "-" + Thread.currentThread().getName());

        // 获取输入流
        try {
            InputStream is = socket.getInputStream();

            // 循环的读取数据
            byte[] buf = new byte[1024];

            while (true) {
                // 打印线程的信息
                System.out.println(Thread.currentThread().getId() + "-" + Thread.currentThread().getName());
                int read = is.read(buf);
                if (read != -1) {
                    System.out.println(new String(buf, 0, read));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭socket
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
