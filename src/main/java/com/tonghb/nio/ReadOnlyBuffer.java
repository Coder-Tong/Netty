package com.tonghb.nio;

import java.nio.ByteBuffer;

/**
 * @author tong
 * @create 2020-11-04-16:47
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        // 创建一个Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        // 向byteBuffer中添加数据
        for (int i = 0; i < 64; ++i) {
            byteBuffer.put((byte) i);
        }

        // 重置position中的值
        byteBuffer.flip();

        // 将byteBuffer设置为只读
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

        // 遍历readOnlyBuffer中的结果
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

        // 向设置为只读的buffer中添加数据，会出现ReadOnlyBufferException
        readOnlyBuffer.put((byte) 65);
    }
}
