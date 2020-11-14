package com.tonghb.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author tong
 * @create 2020-11-04-15:08
 */
public class FileChannelWrite {
    public static void main(String[] args) throws Exception {
        // 待写入的字符串
        String str = "Hello World";

        // 创建一个输出流 -> channel，将数据输入到channel中
        FileOutputStream fileOutputStream = new FileOutputStream("file/file01.txt");

        // 通过fileInputStream获取一个FileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将创建的字符写入到byteBuffer当中
        byteBuffer.put(str.getBytes());

        // 对byteBuffer进行flip
        byteBuffer.flip();

        // 将载有数据的byteBuffer放入到fileChannel中
        fileChannel.write(byteBuffer);

        // 将文件输入流关闭
        fileOutputStream.close();
    }
}
