package com.jlbase.conn.netty

import com.jlbase.conn.handler.HeartBeatIdleStateTrigger
import com.jlbase.conn.handler.ReadHandler
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.timeout.IdleStateHandler
import java.util.concurrent.TimeUnit

/**
 * @Auther: fanqi
 * @datetime: 2024/3/19
 * @desc:
 */
class NettyConnInitializer(private val connConnection: NettyConnConnection) : ChannelInitializer<Channel>() {
    override fun initChannel(ch: Channel?) {
        ch?.let { channel ->
            channel.pipeline()
                .addLast(IdleStateHandler::class.java.simpleName,IdleStateHandler(5,5,0,TimeUnit.SECONDS))
                .addLast("frameEncoder",LengthFieldPrepender(2))
                .addLast("frameDecoder", LengthFieldBasedFrameDecoder(65535,
                    0, 2, 0, 2))
                .addLast(StringDecoder::class.java.simpleName,StringDecoder())
                .addLast(StringEncoder::class.java.simpleName,StringEncoder())
                .addLast(HeartBeatIdleStateTrigger::class.java.simpleName,HeartBeatIdleStateTrigger(connConnection))
                .addLast(ReadHandler::class.java.simpleName,ReadHandler())
        }
    }
}