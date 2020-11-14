package com.tonghb.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

/**
 * @author tong
 * @create 2020-11-10-15:30
 */
public class NewIOClient {
    public static void main(String[] args) throws IOException {
        // 创建一个SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        // 连接服务端
        socketChannel.connect(new InetSocketAddress("localhost", 6666));

        // 文件名
        String filepath = "file/2.jpg";
        FileChannel fileChannel = new FileInputStream(filepath).getChannel();

        // 记录发送的时间
        long startTime = System.currentTimeMillis();

        // 在Linux下一个transferTo方法就可以完成传输
        // 在window下一次调用transferTo只能发送8m, 就需要分段传输文件, 而且要标注传输的位置
        // transfer底层就使用了零拷贝
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送的总的字节数为：" + transferCount + "耗时：" + (System.currentTimeMillis() - startTime));

        // 关闭通道
        fileChannel.close();


    }
}
