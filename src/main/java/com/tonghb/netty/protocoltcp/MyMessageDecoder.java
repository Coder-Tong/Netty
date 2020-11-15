package com.tonghb.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author tong
 * @create 2020-11-15-14:13
 */
public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder被调用");
        // 需要将字节转化为对象进行输出
        int len = in.readInt();
        byte[] content = new byte[len];
        in.readBytes(content);

        // 将数据封装成MessageProtocol，添加到List中供下一个handler进行处理
        out.add(new MessageProtocol(len, content));
    }
}
