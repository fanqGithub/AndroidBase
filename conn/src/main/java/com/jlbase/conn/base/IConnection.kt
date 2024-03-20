package com.jlbase.conn.base

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
    fun sendData(data: String)
}