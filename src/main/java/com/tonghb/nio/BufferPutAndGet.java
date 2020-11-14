package com.tonghb.nio;

import java.nio.ByteBuffer;

/**
 * @author tong
 * @create 2020-11-08-16:31
 */
public class BufferPutAndGet {
    public static void main(String[] args) {
        // 创建一个Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 向Buffer中添加元素
        byteBuffer.putInt(1);
        byteBuffer.putChar('c');
        byteBuffer.putDouble(1.2);
        byteBuffer.putLong(9L);

        // 将buffer中的内容进行反转，将position的值置为0
        byteBuffer.flip();

        // 要按照添加的数据类型取出元素
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getDouble());
        System.out.println(byteBuffer.getLong());
    }
}
