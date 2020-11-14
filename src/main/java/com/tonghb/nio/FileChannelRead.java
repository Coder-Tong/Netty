package com.tonghb.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author tong
 * @create 2020-11-04-15:32
 */
public class FileChannelRead {
    public static void main(String[] args) throws Exception {
        // 创建一个输入流对象
        File file = new File("file/file01.txt");
        FileInputStream inputStream = new FileInputStream(file);

        // 获取一个FileChannel
        FileChannel channel = inputStream.getChannel();

        // 创建一个ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // 将FileChannel的内容写入到byteBuffer
        channel.read(byteBuffer);

        // 将byteBuffer中内容转为字符串
        System.out.println(new String(byteBuffer.array()));

        // 关闭流通道
        inputStream.close();
    }
}
