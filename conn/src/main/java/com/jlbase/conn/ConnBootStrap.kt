package com.jlbase.conn

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

    fun init(){
        //初始化
    }




}