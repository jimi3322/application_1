package com.wayeal.newair.data.bean

data class AQIData(
    val data: AQIDataPoint,
    val result: Int,
    val status: Int
)

data class AQIDataPoint(
    val data: ArrayList<AQIDataXY>,
    val key: String,
    val rangehigh: Int,
    val rangelow: Int
)

data class AQIDataXY(
    val x: String,
    val y: String
)