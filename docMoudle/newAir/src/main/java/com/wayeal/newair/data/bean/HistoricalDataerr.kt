package com.wayeal.newair.data.bean

import com.google.gson.annotations.SerializedName

data class HistoricalData(
    val `data`: List<HistoricalDatas>,
    val result: Int,
    val status: Int,
    val total: Int
)

data class HistoricalDatas(
    val CountyName: String,
    val DataTime: String,
    val ID: String,
    val Name: String,
    @SerializedName("a05024-Avg")
    val a05024Avg: String,
    @SerializedName("a05024-Flag")
    val a05024Flag: String,
    @SerializedName("a05024-Max")
    val a05024Max: String,
    @SerializedName("a05024-Min")
    val a05024Min: String,
    @SerializedName("a21004-Avg")
    val a21004Avg: String,
    @SerializedName("a21004-Flag")
    val a21004Flag: String,
    @SerializedName("a21004-Max")
    val a21004Max: String,
    @SerializedName("a21004-Min")
    val a21004Min: String,
    @SerializedName("a21005-Avg")
    val a21005Avg: String,
    @SerializedName("a21005-Flag")
    val a21005Flag: String,
    @SerializedName("a21005-Max")
    val a21005Max: String,
    @SerializedName("a21005-Min")
    val a21005Min: String,
    @SerializedName("a21026-Avg")
    val a21026Avg: String,
    @SerializedName("a21026-Flag")
    val a21026Flag: String,
    @SerializedName("a21026-Max")
    val a21026Max: String,
    @SerializedName("a21026-Min")
    val a21026Min: String,
    @SerializedName("a34002-Avg")
    val a34002Avg: String,
    @SerializedName("a34002-Flag")
    val a34002Flag: String,
    @SerializedName("a34002-Max")
    val a34002Max: String,
    @SerializedName("a34002-Min")
    val a34002Min: String,
    @SerializedName("a34004-Avg")
    val a34004Avg: String,
    @SerializedName("a34004-Flag")
    val a34004Flag: String,
    @SerializedName("a34004-Max")
    val a34004Max: String,
    @SerializedName("a34004-Min")
    val a34004Min: String
)

data class SelectHistoricalDatas(
    val DataTime: String
)