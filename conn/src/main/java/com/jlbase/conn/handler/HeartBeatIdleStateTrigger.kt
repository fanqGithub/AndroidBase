package com.jlbase.conn.handler

import com.jlbase.conn.Logger
import com.jlbase.conn.netty.NettyConnConnection
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent

/**
 * @Auther: fanqi
 * @datetime: 2024/3/20
 * @desc:心跳机制
 * 用于捕获{@link IdleState#WRITER_IDLE}事件（未在指定时间内向服务器发送数据），然后向<code>Server</code>端发送一个心跳包。
 */
class HeartBeatIdleStateTrigger(private val connection: NettyConnConnection) : ChannelInboundHandlerAdapter(){

    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        Logger.d("HeartBeatIdleStateTrigger#userEventTriggered")
        super.userEventTriggered(ctx, evt)
        if (evt is IdleStateEvent) {
            when (evt.state()) {
                IdleState.READER_IDLE -> {
                    //规定时间内没收到服务端心跳包响应，进行重连操作
                    Logger.d("HeartBeatIdleStateTrigger触发重连")
//                    imsClient.resetConnect(false)
                }
                IdleState.WRITER_IDLE -> {
                    //规定时间内没向服务端发送心跳包，即发送一个心跳包
                    Logger.d("HeartBeatIdleStateTrigger发送心跳包")
                    connection.sendData("心跳包")
                }
                else -> {}
            }
        }
    }
}