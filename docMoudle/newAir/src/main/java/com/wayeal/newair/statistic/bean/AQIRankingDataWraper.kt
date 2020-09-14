package com.wayeal.newair.statistic.bean

import com.google.gson.JsonObject

/**
 * 为了解析方便的类，只在解析后台数据时使用到
 * */
data class AQIRankingDataWraper(val total: Int,
                                val data: JsonObject,
                                val Color: JsonObject)
