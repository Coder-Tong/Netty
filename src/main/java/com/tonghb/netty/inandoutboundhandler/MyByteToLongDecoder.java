package com.tonghb.netty.inandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author tong
 * @create 2020-11-14-13:51
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     *
     * @param ctx  上下文对象
     * @param in   入栈的 ByteBuf
     * @param out  List集合，将解码后的数据传给下一个Handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // Long类型是8个字节， 需要判断有8个字节才能读取
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }

    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("发生了异常：" + cause.getMessage());
    }
}
