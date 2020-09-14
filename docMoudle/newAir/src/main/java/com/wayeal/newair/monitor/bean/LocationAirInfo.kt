package com.wayeal.newair.monitor.bean

import com.google.gson.JsonObject
import java.io.Serializable


data class LocationAirInfo(val Standard: String,
                           val Lng: String,
                           val Lat: String,
                           val ID: String,
                           val Name: String,
                           val Address: String,
                           val SIMTelephone: String,
                           val Comment: String,
                           val ProvinceName: String,
                           val CityName: String,
                           val CountyName: String,
                           val ProvinceID: String,
                           val CityID: String,
                           val CountyID: String,
                           val RegisterTime: String,
                           val Status: Int,
                           val SystemStatus: String,
                           val PollutionType: String,
                           val StandardLevel: String,
                           val IP: String,
                           val DataStatus: Int,
                           val NotifyUsers: List<Any?>,
                           val UpdateStatusDateTime: String,
                           val DataUpdateTime: String,
                           val PrimaryPollutant: String,
                           val AQI: String,
                           val DataVal: JsonObject,
                           var StatusName: String? = null,
                           val DataTime: String) : Serializable