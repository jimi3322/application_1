package com.wayeal.newair.statistic.bean

data class QualityConditionData(val qualityProportion:ArrayList<QualityProportion>)

data class QualityProportion(val item:String
                             ,val num: Int
                             ,val percent: String
                             ,var color: String)