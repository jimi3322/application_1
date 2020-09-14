package com.app.myapplication.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AirUtils {

    companion object {
        fun dip2px(dp: Float): Int {
            val scale = AirApplication.mApp.resources.displayMetrics.density
            return (dp * scale + 0.5f).toInt()
        }

        fun getStringList(res: Int): ArrayList<String>{
            val strings = AirApplication.mApp.resources.getStringArray(res)
            val list = ArrayList<String>()
            for (string in strings){
                list.add(string)
            }
            return list
        }

        /**
         * map转成json
         */
        fun mapTojson(params: HashMap<String, Any>): String {
            val jsonObj = JSONObject(params)
            return jsonObj.toString()
        }

        /**
         * 服务器返回的rgb(0,228,0)
         * */
        fun getRgbInt(rgb: String?): Int{
            if (rgb.isNullOrEmpty()){
                return Color.rgb(255,255,255)
            }
            var tmp = rgb?.substring(4,rgb.length-1)
            Log.i("test", "getRgbInt: = $tmp")
            var arrayRGB = tmp?.split(",")
            return try {
                Color.rgb(arrayRGB?.get(0)!!.toInt(),arrayRGB[1].toInt(),arrayRGB[2].toInt())
            }catch (e: Exception){
                e.printStackTrace()
                Color.rgb(255,255,255)
            }
        }

        /**
         * 根据date转换string
         * */
        @SuppressLint("SimpleDateFormat")
        fun getTime(date: Date): String? { //可根据需要自行截取数据显示
            Log.d("getTime()", "choice date millis: " + date.time)
            val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            return format.format(date)
        }

        @SuppressLint("SimpleDateFormat")
        fun getDate(str: String): Date{
            return try {
                val formatter = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss")
                formatter.parse(str)
            }catch (e: java.lang.Exception){
                Date()
            }
        }

        /**
         * 获取该日期是哪个月 这个月的第一天是周几
         * */
        fun getMonthFirstDay(year: Int,month: Int): Int{
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month - 1)
            calendar.set(Calendar.DATE,1)
            return calendar.get(Calendar.DAY_OF_WEEK)
        }

        /**
         * 获取某月有多少天
         * */
        fun getMonthOfDay(year: Int,month: Int): Int{
            var day = 0
            day = if (year % 4 === 0 && year % 100 !== 0 || year % 400 === 0) {
                29
            } else {
                28
            }
            when (month) {
                1, 3, 5, 7, 8, 10, 12 -> return 31
                4, 6, 9, 11 -> return 30
                2 -> return day
            }

            return 0
        }
    }
}