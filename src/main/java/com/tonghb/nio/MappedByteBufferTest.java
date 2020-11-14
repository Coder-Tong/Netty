package com.tonghb.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author tong
 * @create 2020-11-08-16:51
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws IOException {
        // 读取文件
        RandomAccessFile file = new RandomAccessFile("file/file01.txt", "rw");

        // 获取对应的通道
        FileChannel channel = file.getChannel();

        // 将channel中内容映射到内存当中
        /**
         * 参数1：FileChannel.MapMode.READ_WRITE 使用读写模式
         * 参数2：0 可以直接修改的起始位置
         * 参数3：5 是映射到内存的大小
         * 可以直接修改的范围是 0-5
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        // 在内存中修改文件的值
        mappedByteBuffer.put(0, (byte) 'h');
        mappedByteBuffer.put(3, (byte) 'L');

        System.out.println("修改成功");
    }
}
