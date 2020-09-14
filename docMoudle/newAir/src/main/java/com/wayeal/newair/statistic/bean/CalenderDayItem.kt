package com.wayeal.newair.statistic.bean


/**
 * 日历上显示的点
 * */
data class CalenderDayItem(var isShow: Boolean = false          //该点是否展示，默认为false，不展示时，无法点击，而且是空白
                           ,var dayInfo: AQICalendarDayInfo? = null)    //这里需要展示的东西很多，比如日期 颜色 污染物