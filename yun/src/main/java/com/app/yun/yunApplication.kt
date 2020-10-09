package com.app.yun

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer

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

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this)
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL)

        /*//查看本地SQLite数据库数据工具的初始化
        Stetho.initializeWithDefaults(this)*/
    }
}