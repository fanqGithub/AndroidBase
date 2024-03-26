package com.jlbase.conn.base

import com.jlbase.conn.listener.IMsgSendListener
import com.jlbase.conn.model.Msg

/**
 * @Auther: fanqi
 * @datetime: 2024/3/19
 * @desc:
 */
interface IConnection {

    fun init(serverList: List<String>)

    /**
     * 连接
     */
    fun connect()

    /**
     * 断开连接
     */
    fun disconnect()

    /**
     * 发送数据
     */
    fun sendData(data: Msg,isNeedRetry:Boolean=false, sendListener: IMsgSendListener?=null)
}