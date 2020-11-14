package com.tonghb.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author tong
 * @create 2020-11-12-19:57
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    // 定义一个Channel组，管理所有的channel
    // GlobalEventExecutor 全局事件执行器，是一个单例
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    // 表示连接建立，一旦连接第一个被执行
    // 将当前channel加入到channelGroup中
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 加入到 channelGroup中
        Channel channel = ctx.channel();

        // 将该客户加入聊天的信息发送给群聊中的所有人，channels.writeAndFlush会自动遍历所有的channel，挨个发送消息
        channels.writeAndFlush("[客户端(" + sdf.format(new Date())  + ")]" + channel.remoteAddress() + "加入聊天");

        channels.add(channel);


    }

    // 表示channel处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 提示上线
        System.out.println("[客户端(" + sdf.format(new Date())  + ")]" +ctx.channel().remoteAddress() + "上线了~");
    }

    // 表示channel处于非活动状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 提示离线
        System.out.println("[客户端(" + sdf.format(new Date())  + ")]" + ctx.channel().remoteAddress() + "离线了~");
    }

    // 表示断开连接会被触发，将某某客户离开信息推送给当前在线的用户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        // 当前channel从channels中移除
        channels.writeAndFlush("[客户端(" + sdf.format(new Date())  + ")]" + channel.remoteAddress() + "离开了！");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        // 获取到当前的channel
        Channel channel = channelHandlerContext.channel();

        // 遍历channels，根据不同情况会送不同的消息
        channels.forEach(ch -> {
            if (channel != ch) {  // 发送给别人
                ch.writeAndFlush("[客户(" + sdf.format(new Date())  + ")]" + channel.remoteAddress() + "发送了消息" + s + "\n");
            } else {
                ch.writeAndFlush("[自己(" + sdf.format(new Date())  + ")]发送了消息" + s + "\n");
            }
        });

    }

    // 发生异常，关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}
