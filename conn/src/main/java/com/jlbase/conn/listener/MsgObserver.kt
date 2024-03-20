package com.jlbase.conn.listener

/**
 * @Auther: fanqi
 * @datetime: 2024/3/19
 * @desc:消息观察者
 */
interface MsgObserver {
    fun onNewMsg(msg: String)
}