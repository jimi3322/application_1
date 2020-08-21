package com.app.common.data

import java.io.Serializable

class LeakListData : Serializable{
    var total: String = "0"
    var list: ArrayList<LeakDataItem>? = null
    var dataPageCount: Int = 0

    class LeakDataItem : Serializable{
        var sNum: String? = ""
        var proNum: String? = ""
        var leakRateA: Int? = 0
        var leakRateN: String? = ""
        var alarmThreA: Int? = 0
        var alarmThreN: String? = ""
        var unit: String? = ""
        var alarmResult: String? = ""
        var time: String? = ""
        var leakRate: String? = ""
        var alarmThre: String? = ""
    }
}