package com.jlbase.conn.model

/**
 * @Auther: fanqi
 * @datetime: 2024/3/26
 * @desc:
 */
data class Msg(
    val msg_id:String?=null,
    val content:String?=null
){
    companion object{
        fun newMsg(content: String?):Msg{
           return Msg(msg_id = generateMsgId(),content=content)
        }

        private fun generateMsgId():String{
            return System.currentTimeMillis().toString()
        }
    }

}
