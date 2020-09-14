package com.wayeal.newair.statistic.bean

import com.wayeal.newair.monitor.bean.ComponentBean

/**
 * 从后台返回的AQI日数据，
 * */
data class AQICalendarDayInfo(val color: String                 //背景颜色
                              ,val name: String                 //站点名字
                              ,val time:String                  //时间
                              ,var value:String? = null                 //AQI值
                              ,var PrimaryPollutant:String? = null      //污染物类型
                              ,val componentList:MutableList<ComponentBean> //监测类型以及值
                              ,var componentListString: String? = null)