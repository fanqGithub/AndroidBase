package com.jlbase.conn

import com.jlbase.conn.listener.IMsgSendListener
import com.jlbase.conn.model.Msg
import com.jlbase.conn.netty.NettyConnConnection

/**
 * @Auther: fanqi
 * @datetime: 2024/3/26
 * @desc:上行/消息发送
 */
class Sender {
    fun send(msg:String,retry:Boolean=false,listener: IMsgSendListener?=null){
        NettyConnConnection.instance.sendData(Msg.newMsg(msg),retry,listener)
    }

}