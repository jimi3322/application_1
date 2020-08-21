package com.app.myapplication.common.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.widget.TextView


import org.json.JSONObject

import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import java.util.Random
import java.util.UUID
import java.util.regex.Matcher
import java.util.regex.Pattern

object CommonUtil {
    private val EARTH_RADIUS = 6378137.0

    /**
     * 获取id
     */
    val uuid: String
        get() = UUID.randomUUID().toString()

    /**
     * 获取当前时间
     */
    // HH:mm:ss
    val currentTime: String
        get() {
            val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
            val date = Date(System.currentTimeMillis())
            return simpleDateFormat.format(date)
        }

    /**
     * 获取随机String
     *
     * @return
     */
    val randomString: String
        get() {
            val strings = arrayOf(
                "1001",
                "1002",
                "1003",
                "1004",
                "1005",
                "1006",
                "1007",
                "1008",
                "1009",
                "1010"
            )
            val rd = Random(1)
            val i = (1 + Math.random() * (10 - 1 + 1)).toInt()
            return strings[i - 1]
        }

    /**
     * 单位转换
     */
    //一半大小
    val unit: String
        get() {
            val m3 = SpannableString("Pa.m3/s")
            m3.setSpan(RelativeSizeSpan(0.5f), 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            m3.setSpan(SuperscriptSpan(), 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return m3.toString()
        }


    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 距离：单位为米
     */
    fun distanceOfTwoPoints(
        lat1: Double, lng1: Double,
        lat2: Double, lng2: Double
    ): Double {
        val radLat1 = rad(lat1)
        val radLat2 = rad(lat2)
        val a = radLat1 - radLat2
        val b = rad(lng1) - rad(lng2)
        var s = 2 * Math.asin(
            Math.sqrt(
                Math.pow(Math.sin(a / 2), 2.0) + (Math.cos(radLat1) * Math.cos(radLat2)
                        * Math.pow(Math.sin(b / 2), 2.0))
            )
        )
        s = s * EARTH_RADIUS
        s = (Math.round(s * 10000) / 10000).toDouble()
        return s
    }

    private fun rad(d: Double): Double {
        return d * Math.PI / 180.0
    }

    /**
     * 时间格式化
     */
    fun timeFormat(date: Date?): String {
        if (date == null) {
            return ""
        }
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        return sdf.format(date)
    }

    /**
     * 日期格式化
     */
    fun dateFormat(date: Date?): String {
        if (date == null) {
            return ""
        }
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        return sdf.format(date)
    }

    /**
     * map转成json
     */
    fun mapTojson(params: HashMap<String, Any>): String {
        val jsonObj = JSONObject(params as Map<*, *>)
        return jsonObj.toString()
    }


    fun getRegExpre(type: String, length: Int): String {
        var lengthReg = ""
        if (length > 0) {
            lengthReg = "{0,$length}$"
        }

        if (type === "1") {
            //数字，字母，下划线、汉字
            return "^[\\u4e00-\\u9fa5\\w\\n]$lengthReg"

        }

        if (type === "2") {
            //数字、字母、下划线、中划线、小括号、汉字
            return "^[\\u4e00-\\u9fa5\\w-()（）\\n]$lengthReg"

        }
        if (type === "4") {
            //电话号码校验

            /** 
             * 判断字符串是否符合手机号码格式 
             * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188 
             * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186 
             * 电信号段: 133,149,153,170,173,177,180,181,189 
             */
            return "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$"// "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
        }

        return if (type === "5") {
            //数字、字母、下划线、中划线、小括号、汉字，人名以“、”号隔开
            "^[\\u4e00-\\u9fa5\\w-()（）、\\n]$lengthReg"
        } else ""
    }


    //判断字符串是否满足正则表达式
    fun isMatcher(str: String, regular: String): Boolean {
        val pt = Pattern.compile(regular)
        val mt = pt.matcher(str)
        return mt.matches()
    }

    /**
     * 将二进制流转换成bitmap
     */
    fun getBitmapFromByte(streamByte: ByteArray?): Bitmap? {
        return if (streamByte == null) {
            null
        } else BitmapFactory.decodeByteArray(streamByte, 0, streamByte.size)
    }

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getMD5Str(str: String): String {
        try {
            // 生成一个MD5加密计算摘要
            val md = MessageDigest.getInstance("MD5")
            // 计算md5函数
            md.update(str.toByteArray())
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return BigInteger(1, md.digest()).toString(16)
        } catch (e: Exception) {
            throw Exception("MD5加密出现错误，$e")
        }

    }

    /**
     * 单位转换
     */
    fun setUnit(textView: TextView) {
        val m3 = SpannableString("Pa.m3/s")
        m3.setSpan(RelativeSizeSpan(0.7f), 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)//一半大小
        m3.setSpan(SuperscriptSpan(), 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = m3
    }

}
