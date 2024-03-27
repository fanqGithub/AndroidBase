package com.jlbase.conn.listener

/**
 * @Auther: fanqi
 * @datetime: 2024/3/26
 * @desc:
 */
interface IMsgSendListener<M> {
    fun onSendSuccess(msg:M)
    fun onSendFail(msg: M,error:Throwable?)
}