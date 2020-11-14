package com.tonghb.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author tong
 * @create 2020-11-12-19:07
 */
public class NettyByteBuffer {
    public static void main(String[] args) {
        // 1. 创建一个ByteBuf对象，该对象包含一个数组arr，为一个byte[10]
        // 2. 在netty的buffer中，不需要使用 flip 进行反转，因为其底层维护了一个readIndex和一个writeIndex
        // 0 - readIndex 是被遗弃的区域
        // readIndex - writeIndex 是可读区域
        // writeIndex - capacity 是可写的区域
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; ++i) {
            buffer.writeByte(i);
        }

        // 输出
        for (int i = 0; i < buffer.capacity(); ++i) {
            System.out.println(buffer.readerIndex(i));
        }
    }
}
