package com.jlbase.conn.handler

import com.jlbase.conn.netty.NettyConnConnection
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

/**
 * @Auther: fanqi
 * @datetime: 2024/3/19
 * @desc:用于处理连接和数据传输
 */
class ConnLinkHandler(private val connConnection: NettyConnConnection):SimpleChannelInboundHandler<String>() {

    /**
     * 向服务端发送鉴权令牌
     */
    override fun channelActive(ctx: ChannelHandlerContext?) {
        super.channelActive(ctx)
    }

    /**
     * 连接断开了
     */
    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
    }

    /**
     * 发生异常
     */
    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        super.exceptionCaught(ctx, cause)
    }

    /**
     * 收到的消息
     */
    override fun channelRead0(ctx: ChannelHandlerContext?, msg: String?) {

    }
}