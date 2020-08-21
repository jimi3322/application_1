package com.app.yun

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

//由于从ViewModel层就开始不持有Activity引用，会出现缺Context的情况，所以给这个加上全局的Context来获取Context对象
class yunApplication: Application() {
    companion object{
        const val TOKEN = "TnYYjtXPteDySfRK"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}