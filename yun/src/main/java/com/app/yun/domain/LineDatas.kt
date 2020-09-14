package com.app.yun.domain

data class LineDatas(
    val `data`: List<LineData>,
    val result: Int,
    val status: Int,
    val total: Int
)

data class LineData(
    val `data`: List<LineDataX>,
    val key: String
)

data class LineDataX(
    val x: Long,
    val y: String
)