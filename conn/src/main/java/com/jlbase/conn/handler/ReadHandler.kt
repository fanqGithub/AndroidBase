package com.jlbase.conn.handler

import com.jlbase.conn.Logger
import com.jlbase.conn.MsgDispatcher
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

/**
 * @Auther: fanqi
 * @datetime: 2024/3/20
 * @desc:
 */
class ReadHandler : ChannelInboundHandlerAdapter() {

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        super.channelRead(ctx, msg)
        Logger.d("Client,接收到服务端发来的消息:$msg")
        msg?.let { MsgDispatcher.instance.dispatchMsg(it.toString()) }
    }

}