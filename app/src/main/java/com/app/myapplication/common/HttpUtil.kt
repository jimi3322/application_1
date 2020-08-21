package com.app.myapplication.common

import android.util.Log
import com.app.myapplication.common.domain.Constant
import okhttp3.OkHttpClient
import okhttp3.Request

object HttpUtil {
    fun sendOKHttpRequest(address:String,callback:okhttp3.Callback){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(address)
            .build()
        //enqueue方法在内部开启子线程在，子线程中进行HTTP请求，最后的结果返回到callback里
        client.newCall(request).enqueue(callback)
    }

    fun getServiceAddress(serviceMethod: String): String {
        val ip = Constant.SERVICE_IP
        val port = Constant.SERVICE_PORT
        return Constant.HTTP_HEAD + ip + Constant.ID_COLON_EN + port + Constant.SERVICE_API + Constant.ID_LEFT_SLASH + serviceMethod
    }
}