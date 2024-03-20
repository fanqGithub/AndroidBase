package com.jlbase.conn

import com.jlbase.conn.listener.MsgObserver

/**
 * @Auther: fanqi
 * @datetime: 2024/3/20
 * @desc:消息/事件分发者
 */
class MsgDispatcher private constructor(){

    companion object{
        val instance: MsgDispatcher by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MsgDispatcher()
        }
    }

    private val observerList= mutableListOf<MsgObserver>()

    /**
     * 注册监听器
     */
    fun registerObserver(observer: MsgObserver){
        observerList.add(observer)
    }

    /**
     * 移除
     */
    fun removeObserver(observer: MsgObserver){
        observerList.remove(observer)
    }

    /**
     * 分发
     */
    fun dispatchMsg(msg: String){
        observerList.forEach {
            it.onNewMsg(msg)
        }
    }

}