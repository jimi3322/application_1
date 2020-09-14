package com.wayeal.newair.monitor.bean


data class ConfigItem(val ComponentID: String,
                     val Type: String,
                     val CheckElems: List<String>,
                     val ComponentVal: List<ComponentValBean>,
                     val FactorType: String,
                     val CreateTime: String)

data class ComponentValBean(val Index: Int,
                            val ComponentID: String,
                            val key: String)