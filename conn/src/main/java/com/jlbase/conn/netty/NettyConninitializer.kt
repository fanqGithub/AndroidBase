package com.jlbase.conn.netty

import com.jlbase.conn.handler.HeartBeatIdleStateTrigger
import com.jlbase.conn.handler.ReadHandler
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.timeout.IdleStateHandler
import java.util.concurrent.TimeUnit

/**
 * @Auther: fanqi
 * @datetime: 2024/3/19
 * @desc:
 */
class NettyConnInitializer(private val connConnection: NettyConnConnection) : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel?) {
        ch?.let { channel ->
            channel.pipeline()
                .addLast(IdleStateHandler::class.java.simpleName,IdleStateHandler(0,0,5,TimeUnit.SECONDS))
                .addLast("frameEncoder",LengthFieldPrepender(2))
                .addLast("frameDecoder", LengthFieldBasedFrameDecoder(65535,
                    0, 2, 0, 2))
                .addLast(HeartBeatIdleStateTrigger::class.java.simpleName,HeartBeatIdleStateTrigger())
                .addLast(ReadHandler::class.java.simpleName,ReadHandler())
        }
    }
}