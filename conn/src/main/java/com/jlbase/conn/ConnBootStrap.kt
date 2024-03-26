package com.jlbase.conn

import com.jlbase.conn.netty.NettyConnConnection
import com.jlbase.conn.utils.Logger
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
    private var mApkSession=""
    private var mUid=""


    fun init(apkSession: String, uId:String):ConnBootStrap{
        //初始化
        if (hasInit.compareAndSet(false,true)){
            NettyConnConnection.instance.init(fetchServerList())
            mApkSession=apkSession
            mUid=uId
        }else{
            Logger.d("ConnBootStrap has been initialized")
        }
        return this
    }

    /**
     *
     */
    fun startConnect(){
        checkInit()
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

    /**
     * 断开连接
     */
    fun disConnect(){
        checkInit()
        NettyConnConnection.instance.disconnect()
    }

    private fun checkInit(){
        if (!hasInit.get()){
            throw IllegalStateException("ConnBootStrap has not been initialized")
        }
    }


}