package com.tonghb.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * @author tong
 * @create 2020-11-12-19:33
 */
public class NettyByteBuffer02 {
    public static void main(String[] args) {
        // 创建ByteBuf
        ByteBuf byteBuff = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);

        // 使用相关方法
        if (byteBuff.hasArray()) {
            byte[] array = byteBuff.array();

            System.out.println(new String(array, CharsetUtil.UTF_8));

            // 输出结果
            System.out.println(byteBuff.arrayOffset());  // 0
            System.out.println(byteBuff.readerIndex());  // 0
            System.out.println(byteBuff.writerIndex());  // 11
            System.out.println(byteBuff.readableBytes()); // 11
            System.out.println(byteBuff.capacity()); // 64

            // 按照某个范围进行读取
            System.out.println(byteBuff.getCharSequence(4, 6, CharsetUtil.UTF_8));
        }
    }
}
