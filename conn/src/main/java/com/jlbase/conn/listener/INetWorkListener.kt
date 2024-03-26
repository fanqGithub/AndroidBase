package com.jlbase.conn.listener

/**
 * @Auther: fanqi
 * @datetime: 2024/3/26
 * @desc:
 */
interface INetWorkListener {

    fun onNetworkAvailable()

    fun onNetworkUnavailable()
}