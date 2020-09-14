package com.wayeal.newair.statistic.bean


data class ChartData(val key: String,
                     val data: List<DataBean>)

data class DataBean(val x: Float,
                    val y: String?)