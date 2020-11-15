package com.tonghb.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author tong
 * @create 2020-11-15-14:10
 */
public class MyMessageEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        // 得到数据的长度
        System.out.println("MyMessageEncoder被调用");
        // 将数据的长度发送出去
        out.writeInt(msg.getLen());
        // 将数据的内容发送出去
        out.writeBytes(msg.getContent());
    }
}
