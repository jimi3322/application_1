package com.app.yun.data

data class YunRealtimeData(var result: YunRealtimeInfo? = null)

data class YunRealtimeInfo(var realtime: RealTime? = null)

data class RealTime(
    var status: String
    , var temperature: Double? = 0.0
    , var humidity: Double? = 0.0
    , var skycon: String? = "-"
    , var pressure: Double? = 0.0
)