package com.wayeal.newair.statistic.bean


data class SamePeriodData(val LastYear: List<LastYearBean>,
                     val ThisYear: List<ThisYearBean>) {

    data class LastYearBean(val Level: String,
                            val Ratio: String,
                            val SampleNumber: Int)

    data class ThisYearBean(val Level: String,
                            val Ratio: String,
                            val SampleNumber: Int)
}