package com.wayeal.newair.monitor.bean

import com.wayeal.newair.common.bean.BaseChooseItemType
import com.wayeal.newair.common.bean.ChooseItemType
import java.io.Serializable

data class TestPoint(
        val title: String,
        var type:String? = "province",
        var name:String? = "ProvinceID",
        var key:String? = "44",
        var children:ArrayList<CityPoint>?
): Serializable

data class CityPoint(
        val title: String
        , var type: String? = "city"
        , var name: String? = "CityID"
        , var key: String? = "4401"
        , var latitude:String? = ""
        , var longitude:String? = ""
        , var children: ArrayList<CountyPoint>?
): Serializable

data class CountyPoint(
        val title: String
        , var type: String? = "county"
        , var name: String? = "CountyID"
        , var key: String? = "440112"
        , var children: ArrayList<TestPointDetail>?
): Serializable

data class TestPointDetail(
        val title: String
        , var type: String? = "county"
        , var name: String? = "CountyID"
        , var key: String? = "440112"
        , var latitude:String? = ""
        , var longitude:String? = ""
        , var pollutionType: String? = "440112"
        , var status: Int? = 0
): Serializable,BaseChooseItemType() {
    override fun returnType(): ChooseItemType {
        return ChooseItemType.station
    }
}

