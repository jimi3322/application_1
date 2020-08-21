package com.app.yun.domain


data class ShowPieChartData(var data: ArrayList<ShowPieChartDatas>? = null)
data class ShowPieChartDatas(var datas: ArrayList<Artical>)
data class Artical(
    var author: String
    ,var title:String
)