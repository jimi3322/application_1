package com.wayeal.newair.monitor.bean

import com.google.gson.JsonObject
import java.io.Serializable

data class RealTimeListData(val Standard: String,
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
                            val UpdateStatusDateTime: String,
                            val DataUpdateTime: String,
                            val AirQualityVal: List<AirQualityValBean>,
                            val Priority: String,
                            val Level: String,
                            val StatusName: String,
                            val Color: String,
                            val Effect: String,
                            val Measures: String,
                            val PrimaryPollutant: String,
                            val AQI: Int,
                            val DataTime: String):Serializable



data class AirQualityValBean(val Priority: String,
                             val Level: String,
                             val StatusName: String,
                             val Color: String,
                             val Effect: String,
                             val Measures: String,
                             val PrimaryPollutant: String,
                             val AQI: Int,
                             val DataTime: String):Serializable
