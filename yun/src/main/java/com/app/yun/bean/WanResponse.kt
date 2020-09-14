package com.app.yun.bean

data class WanResponse<out T>(val result: Int, val status: Int, val data: T,val DataTime: String)