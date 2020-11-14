package com.tonghb.nio;

import java.nio.IntBuffer;

/**
 * @author tong
 * @create 2020-11-04-13:50
 */

/**
 * Buffer类中共包含有四个属性来提供关于其所包含数据元素的信息
 * Capacity: 容量，即可以容纳的最大数据量，在缓冲区创建时被设定并且不能改变
 * Limit: 表示缓冲区的当前终点，不能对缓冲区超过极限位置进行读取，且极限是可以修改的
 * Position: 位置，下一个要被读或写的元素的索引，每次读写缓冲区数据时都会改变值，为下次写做准备
 * Mark: 标记
 */
public class BasicBuffer {
    public static void main(String[] args) {
        // 创建一个Buffer，大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 向buffer中存放数据
        for (int i = 0; i < intBuffer.capacity(); ++i) {
            intBuffer.put(i * 2);
        }

        // 切换buffer的模式,本质上是将buffer底层的数组的position参数设置为0
        intBuffer.flip();

        // Buffer 常用函数
        intBuffer.position(1);   // 设置position的值，从第1个元素开始读
        intBuffer.limit(3);   // 设置limit的值，最多读到limit的位置


        // 去除buffer中的元素
        while (intBuffer.hasRemaining()) {
            // get()函数会自动地将索引值自增
            System.out.println(intBuffer.get());
        }
    }
}
