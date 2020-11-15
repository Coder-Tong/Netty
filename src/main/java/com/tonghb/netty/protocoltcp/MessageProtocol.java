package com.tonghb.netty.protocoltcp;

/**
 * @author tong
 * @create 2020-11-15-14:00
 */
// 协议包
public class MessageProtocol {
    // 设置相关属性
    private int len;   // 数据包的长度
    private byte[] content;    // 数据包的内容

    public MessageProtocol(int len, byte[] content) {
        this.len = len;
        this.content = content;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
