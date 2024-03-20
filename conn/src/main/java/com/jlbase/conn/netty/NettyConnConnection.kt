package com.jlbase.conn.netty

import com.jlbase.conn.Logger
import com.jlbase.conn.MsgDispatcher
import com.jlbase.conn.base.ConnConfig
import com.jlbase.conn.base.IConnection
import com.jlbase.conn.listener.ConnectStatusObserver
import com.jlbase.conn.listener.MsgObserver
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.concurrent.ScheduledFuture

/**
 * @Auther: fanqi
 * @datetime: 2024/3/19
 * @desc:基于Netty+TCP
 */
class NettyConnConnection private constructor(): IConnection {

    private val workerGroup = NioEventLoopGroup(4)
    private var channel: Channel? = null
    private var bootstrap: Bootstrap? = null
    //连接超时时间
    private val connectTimeout: Int = ConnConfig.DEFAULT_CONNECT_TIMEOUT
    //是否已连接
    private var isConnected = false
    private val statusObservers= mutableListOf<ConnectStatusObserver>()
    private var serverList= mutableListOf<String>()

    companion object{
        val instance: NettyConnConnection by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NettyConnConnection()
        }
    }

    override fun init(list: List<String>) {
        this.serverList.clear()
        this.serverList.addAll(list)
    }

    /**
     * 连接
     */
    override fun connect() {
        bootstrap = Bootstrap().group(workerGroup)
            .channel(NioSocketChannel::class.java)
            // 设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.TCP_NODELAY, true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
            .handler(NettyConnInitializer(this))
        if (serverList.isEmpty()){
            Logger.d("未设置服务器地址")
            onConnectStatusCallback(ConnConfig.CONNECT_STATE_FAILURE)
            return
        }
        serverList.forEach breaking@{ hostPort->
            if (isConnected||(channel!=null && channel?.isActive==true)){
                Logger.d("服务器已连接且活跃，无需连接")
                return@breaking
            }
            val hostPortArray=hostPort.split(":")
            if (hostPortArray.size!=2){
                Logger.d("服务器地址格式错误")
                onConnectStatusCallback(ConnConfig.CONNECT_STATE_FAILURE)
                return
            }
            val host=hostPortArray[0]
            val port=hostPortArray[1].toInt()
            connectToServer(host,port)
        }
    }

    /**
     * 连接到服务器
     */
    private fun connectToServer(host: String, port: Int) {
        runCatching {
            onConnectStatusCallback(ConnConfig.CONNECT_STATE_CONNECTING)
            bootstrap?.connect(host, port)?.addListener(object : ChannelFutureListener {
                override fun operationComplete(future: ChannelFuture?) {
                    future?.let { channelFuture ->
                        if (channelFuture.isSuccess) {
                            channel = channelFuture.channel()
                            isConnected = true
                            onConnectStatusCallback(ConnConfig.CONNECT_STATE_SUCCESSFUL)
                            Logger.d(String.format("连接Server(host=[%s], port=[%s])成功", host, port))
                        } else {
                            isConnected = false
                            channel = null
                            channelFuture.channel().close()
                            workerGroup.shutdownGracefully()
                            onConnectStatusCallback(ConnConfig.CONNECT_STATE_FAILURE)
                        }
                    } ?: run {
                        channel = null
                        workerGroup.shutdownGracefully()
                        Logger.d(String.format("连接Server(host=[%s], port=[%s])失败", host, port))
                        onConnectStatusCallback(ConnConfig.CONNECT_STATE_FAILURE)
                    }
                }
            })?.sync()
        }.onFailure {
            channel = null
            workerGroup.shutdownGracefully()
            onConnectStatusCallback(ConnConfig.CONNECT_STATE_FAILURE)
            Logger.d(String.format("连接Server(host=[%s], port=[%s])失败,错误=[%s]", host, port, it.message))
        }
    }

    /**
     * 断开
     */
    override fun disconnect() {
        channel?.close()
        workerGroup.shutdownGracefully()
    }

    /**
     * 发送数据
     */
    override fun sendData(msg: String) {
        if (!isConnected) {
            Logger.d("未连接到服务器，无法发送数据")
            return
        }
        runCatching {
            channel?.writeAndFlush(msg)?.addListener { future ->
                if (future.isSuccess) {
                    Logger.d("发送数据成功")
                } else {
                    Logger.d("发送数据失败")
                }
            }
        }.onFailure {
            Logger.d("发送数据失败，错误=[%s]".format(it.message))
        }
    }

    /**
     * 添加状态观察者
     */
    fun registerConnStateObserver(statusObserver: ConnectStatusObserver){
        statusObservers.add(statusObserver)
    }

    /**
     * 移除状态观察者
     */
    fun unregisterConnStateObserver(statusObserver: ConnectStatusObserver){
        statusObservers.remove(statusObserver)
    }

    fun registerMsgObserver(msgObserver: MsgObserver){
        MsgDispatcher.instance.registerObserver(msgObserver)
    }

    fun unregisterMsgObserver(msgObserver: MsgObserver){
        MsgDispatcher.instance.removeObserver(msgObserver)
    }

    /**
     * 连接状态设置
     */
    fun onConnectStatusCallback(connectStatus:Int) {
        when(connectStatus){
            ConnConfig.CONNECT_STATE_SUCCESSFUL->{
                statusObservers.forEach {
                    it.onConnected()
                }
            }
            ConnConfig.CONNECT_STATE_FAILURE->{
                statusObservers.forEach {
                    it.onConnectFailed()
                }
            }
            ConnConfig.CONNECT_STATE_CONNECTING->{
                statusObservers.forEach {
                    it.onConnecting()
                }
            }
            else->{ Logger.d("CONNECT_STATE_FAILURE") }
        }
    }

    /**
     * 移除指定handler
     * @param handlerName
     */
    private fun removeHandler(handlerName: String) {
        channel?:return
        runCatching {
            if (channel!!.pipeline()[handlerName] != null) {
                channel!!.pipeline().remove(handlerName)
            }
        }.onFailure{
            it.printStackTrace()
            Logger.d("移除handler失败，handlerName=$handlerName")
        }
    }

}