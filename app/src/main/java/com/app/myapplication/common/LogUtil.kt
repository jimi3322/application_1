package com.app.myapplication.common

import android.util.Log

//日志类工具，在发布时所有日志被屏蔽
object LogUtil {
    private const val VERBOSE = 1
    private const val DEBUG = 2
    private const val INFO = 3
    private const val WARN = 4
    private const val ERROR = 5
    private val level = VERBOSE

    fun v(tag:String,msg:String) {
        if (level<= VERBOSE){
            Log.v(tag,msg)
        }
    }
    

    fun d(tag:String,msg:String) {
        if (level<= DEBUG){
            Log.v(tag,msg)
        }
    }
    fun i(tag:String,msg:String) {
        if (level<= INFO){
            Log.v(tag,msg)
        }
    }
    fun w(tag:String,msg:String) {
        if (level<= WARN){
            Log.v(tag,msg)
        }
    }
    fun e(tag:String,msg:String) {
        if (level<= ERROR){
            Log.v(tag,msg)
        }
    }
}