package com.tonghb.test;


import io.netty.util.NettyRuntime;
import org.junit.Test;

/**
 * @author tong
 * @create 2020-11-05-19:04
 */
public class Tests {
    @Test
    public void testNettyDefaultThreadNum() {
        System.out.println(NettyRuntime.availableProcessors() * 2);
    }

    @Test
    public void test2() {
        String 同 = "Hello";
        System.out.println(同);
    }
}
