package com.wayeal.newair.statistic.bean

import com.google.gson.JsonObject

/**
 * 为了解析方便的类，只在解析后台数据时使用到
 * */
data class AQIDayDataWraper(val total: Int,
                     val externalData: List<JsonObject>)
