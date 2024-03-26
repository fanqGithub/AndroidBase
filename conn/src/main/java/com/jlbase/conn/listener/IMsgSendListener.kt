package com.jlbase.conn.listener

/**
 * @Auther: fanqi
 * @datetime: 2024/3/26
 * @desc:
 */
interface IMsgSendListener {
    fun <M> onSendSuccess(msg:M)
    fun <M,E> onSendFail(msg: M,error:E)
}