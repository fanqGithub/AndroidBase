package com.jlbase.conn

import com.jlbase.conn.netty.NettyConnConnection
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @Auther: fanqi
 * @datetime: 2024/3/19
 * @desc:
 */
class ConnBootStrap private constructor(){

    companion object{
        val instance:ConnBootStrap by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ConnBootStrap()
        }
    }

    private val hasInit=AtomicBoolean(false)


    fun init(apkSession: String, uId:String):ConnBootStrap{
        //初始化
        if (hasInit.compareAndSet(false,true)){
            NettyConnConnection.instance.init(fetchServerList())
        }
        return this
    }

    fun start(){
        NettyConnConnection.instance.connect()
    }


    /**
     * 根据业务传入的业务信息获取服务列表
     */
    private fun fetchServerList():List<String>{
        return mutableListOf(
            "192.168.155.111:8855"
        )
    }


}