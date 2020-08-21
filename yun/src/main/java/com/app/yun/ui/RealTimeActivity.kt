package com.app.yun.ui

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.myapplication.common.LogUtil
import com.app.myapplication.common.utils.http.GsonResponseHandler
import com.app.myapplication.common.utils.http.OkHttpUtils
import com.app.yun.R
import com.app.yun.data.RealTime
import com.app.yun.data.YunRealtimeData
import kotlinx.android.synthetic.main.activity_real_time.*
import kotlinx.android.synthetic.main.including_main_bottom_info_text.view.*
import java.util.*

class RealTimeActivity : AppCompatActivity() {
//    private var mShowRealTimeData: ShowRealTimeData?=null
    private var refreshTimer: Timer? = null
    private val GET_REFRESH_TIMER_MESSAGE = 100

    val mHandler = object :Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg?.what){
                GET_REFRESH_TIMER_MESSAGE -> {
                    getRealTimedata()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_real_time)

        initView()
        getRealTimedata()
    }

    private fun initView(){
        humidity.title.text = resources.getString(R.string.humidity)
        skycon.title.text = resources.getString(R.string.skycon)
    }
    private fun getRealTimedata(){

        val URL = "https://api.caiyunapp.com/v2.5/TnYYjtXPteDySfRK/121.6544,25.1552/realtime.json"
        OkHttpUtils.getInstance().get(
            this,
            URL,
            object :GsonResponseHandler<YunRealtimeData>(){
            override fun onSuccess(statusCode: Int, data: YunRealtimeData?) {
               // startTimer(0)
                LogUtil.d("YunRealtimeData",":"+data.toString())
                handleData(data?.result?.realtime)
            }

            override fun onFailure(statusCode: Int, error_msg: String?) {
                LogUtil.d("onFailure",error_msg.toString())
                Toast.makeText(this@RealTimeActivity,"请求失败", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun handleData(data: RealTime?){
        humidity.desc.text = "${data?.humidity?:"--"}"
        skycon.desc.text = "${data?.skycon?:"--"}"


    }

    /**
     * 启动定时器
     *
     * @param delayMilliseconds 定时器启动延迟毫秒数，0表示立即启动
     */
    private fun startTimer(delayMilliseconds: Int) {

        if(refreshTimer !=null){
            refreshTimer?.cancel()
            refreshTimer?.purge()
            refreshTimer = null
        }
        refreshTimer = Timer()
        refreshTimer?.schedule(object : java.util.TimerTask() {
            override fun run() {
                val message = mHandler.obtainMessage()
                message.what = GET_REFRESH_TIMER_MESSAGE
                mHandler.sendMessage(message)
            }
        }, delayMilliseconds.toLong(),  2000L)
    }
    /**
     * 停止定时器
     */
    private fun stopTimer() {
        refreshTimer?.cancel()
        refreshTimer?.purge()
        refreshTimer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }
}
