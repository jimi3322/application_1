package com.wayeal.newair.monitor.bean

import com.wayeal.newair.common.bean.BaseChooseItemType
import com.wayeal.newair.common.bean.ChooseItemType

data class ComponentBean(val ComponentID: String,
                     val Description: String,
                     val Unit: String,
                     val CreateTime: String,
                     var Value: String,        //加个值Key 这个值在月度日历数据里会使用
                     val Type: String):BaseChooseItemType(){
    override fun returnType(): ChooseItemType {
        return ChooseItemType.ComponentBean
    }
}