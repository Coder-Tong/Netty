package com.tonghb.nio;

/**
 * @author tong
 * @create 2020-11-04-15:44
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 功能：将file01.txt中的文件写入到file02.txt中
 */
public class FileChannelReadAndWrite {
    public static void main(String[] args) throws Exception {
        // 创建一个输入流和输出流
        FileInputStream inputStream = new FileInputStream("file/file01.txt");
        FileOutputStream outputStream = new FileOutputStream("file/file02.txt");

        // 得到对应的FileChannel
        FileChannel inputStreamChannel = inputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();

        // 创建一个ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        // 读取和写入数据
        while (true) {
            // 先将byteBuffer中内容清空
            byteBuffer.clear();

            // 将输入流中的内容写入到byteBuffer中
            int read = inputStreamChannel.read(byteBuffer);
            if (read == -1) {  // 文件读取完毕
                break;
            }

            // 将byteBuffer中的内容反转并写入输出channel中
            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
        }

        // 关闭流
        inputStreamChannel.close();
        outputStreamChannel.close();
    }
}
