package com.tonghb.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author tong
 * @create 2020-11-12-21:27
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 判断事件的类型
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            String eventType = null;

            // 判读是哪种事件
            switch (event.state()) {
                case READER_IDLE: eventType = "读空闲"; break;
                case WRITER_IDLE: eventType = "写空闲"; break;
                case ALL_IDLE: eventType = "读写空闲"; break;
            }

            System.out.println(ctx.channel().remoteAddress() + " ---超时事件发生：" + eventType);
            System.out.println("服务器做相应的处理");

            // 发生空闲，关闭通道
            ctx.channel().close();
        }
    }
}
