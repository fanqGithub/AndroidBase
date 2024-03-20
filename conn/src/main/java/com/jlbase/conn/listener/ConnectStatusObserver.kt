package com.jlbase.conn.listener

/**
 * @Auther: fanqi
 * @datetime: 2024/3/19
 * @desc:长链接-连接状态回调
 */
interface ConnectStatusObserver {
    /**
     * 正在连接
     */
    fun onConnecting()

    /**
     * 连接成功
     */
    fun onConnected()

    /**
     * 连接失败
     */
    fun onConnectFailed()


}