package com.app.yun.domain

import android.content.Context
import com.app.yun.R
import com.app.yun.data.YunRealtimeInfo

class ShowRealTimeData {
    var sysParam1:MutableList<ShowItem>?=null
    val TITLE_TYPE_1 = "1"
    /**
     * 把接口返回的数据处理成页面可以直接使用的数据格式
     */
    fun handleData(context: Context, data: YunRealtimeInfo?){
        if(sysParam1 == null){
            sysParam1 = ArrayList()
        }
        sysParam1?.clear()
        sysParam1?.apply {
            add(ShowItem3().apply {
                id = TITLE_TYPE_1
                title = "实况天气获取"
            })
            add(ShowItem1(context).apply {
//                value = data?.temperature.toString() ?: "--"
                title = context.resources.getString(R.string.temperature)
            })


        }

    }
    open class ShowItem{
        var show = true
        var type=0
        var title:String?=""
    }
    /**
     * top title
     */
    class ShowItem3: ShowItem{
        constructor(){
            type = 3
        }
        var id = ""
        var rotation = 0.0f
    }
    /**
     * 只展示title 和 相应的值
     */
    class ShowItem1: ShowItem{
        constructor(context:Context){
            type = 1
            textColor = context.resources.getColor(R.color.commonTextGray)
        }
        var value:String?="--"
        var unit = ""
        var textColor:Int=0
    }
}