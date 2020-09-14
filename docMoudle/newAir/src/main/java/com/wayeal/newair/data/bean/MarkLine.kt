package com.wayeal.newair.data.bean


data class MarkLineData(val dataAve:ArrayList<MarkAve>,val dataRang:ArrayList<MarkRang>,val datalabel:ArrayList<Label>)

data class MarkAve(
        val name:String,
        val type:String,
        val label: Label
)

data class MarkRang(
        val name:String,
        val yAxis:String,
        val label:Label
)


data class Label(
        val position:String,
        val show:Boolean,
        val formatter:String
)
