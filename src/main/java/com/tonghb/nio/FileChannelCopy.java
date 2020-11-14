package com.tonghb.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author tong
 * @create 2020-11-04-16:26
 */
public class FileChannelCopy {
    public static void main(String[] args) throws Exception {
        // 创建一个输入流和一个输出流
        FileInputStream inputStream = new FileInputStream("file/1.jpg");
        FileOutputStream outputStream = new FileOutputStream("file/2.jpg");

        // 得到对应的channel
        FileChannel inputStreamChannel = inputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();

        // 调用channel的transform方法
        outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size());

        // 关闭流通道
        inputStreamChannel.close();
        outputStreamChannel.close();
        inputStream.close();
        outputStream.close();
    }
}
