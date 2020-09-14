package com.wayeal.newair.common.bean

 abstract class BaseChooseItemType(var check: Boolean = false) {

     abstract fun returnType() : ChooseItemType

 }

enum class ChooseItemType{
    TableHeaderInfo,station,ComponentBean
}