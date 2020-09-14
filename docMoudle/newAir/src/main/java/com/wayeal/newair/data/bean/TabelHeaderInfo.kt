package com.wayeal.newair.data.bean

import com.wayeal.newair.common.bean.BaseChooseItemType
import com.wayeal.newair.common.bean.ChooseItemType

data class TabelHeaderInfo(
    val data: ArrayList<TabelHeaderData>,
    val result: Int,
    val status: Int,
    val total: Int
)

data class TabelHeaderData(
    val ComponentID: String,
    val CreateTime: String,
    val DecimalPlaces: String,
    val Description: String,
    val Standard: String,
    val StandardVal: List<StandardVal>,
    val Type: String,
    val Unit: String,
    val abnormal: String,
    val abnormalhigh: Int,
    val abnormallow: Int,
    val range: String,
    val rangehigh: Int,
    val rangelow: Int
):BaseChooseItemType() {
    override fun returnType(): ChooseItemType {
        return ChooseItemType.TableHeaderInfo
    }
}

data class StandardVal(
    val Assessment: String,
    val ComponentID: String,
    val CreateTime: String,
    val DensityLimit: String,
    val IAQI: Int,
    val Level: String,
    val LevelName: String,
    val Priority: Int,
    val RangeHigh: String,
    val RangeLow: String,
    val SettingType: String,
    val StandardId: String,
    val Type: String
)