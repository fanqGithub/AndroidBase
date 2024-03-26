package com.gzik.jlbase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gzik.jlbase.ui.theme.JLBaseTheme
import com.jlbase.conn.ConnBootStrap
import com.jlbase.conn.utils.Logger
import com.jlbase.conn.MsgDispatcher
import com.jlbase.conn.listener.MsgObserver

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JLBaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        ConnBootStrap.instance.init("","").start()
        MsgDispatcher.instance.registerObserver(object : MsgObserver{
            override fun onNewMsg(msg: String) {
                //处理消息
                Logger.d("客户端收到服务侧过来的消息：$msg")
            }
        })
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JLBaseTheme {
        Greeting("Android")
    }
}