package com.jlbase.conn.base

/**
 * @Auther: fanqi
 * @datetime: 2024/3/19
 * @desc:
 */
object ConnConfig {
    // 连接超时时长
    val DEFAULT_CONNECT_TIMEOUT = 10 * 1000

    //连接状态：连接中
    const val CONNECT_STATE_CONNECTING = 0

    //连接状态：连接成功
    const val CONNECT_STATE_SUCCESSFUL = 1

    //连接状态：连接失败
    const val CONNECT_STATE_FAILURE = -1
}