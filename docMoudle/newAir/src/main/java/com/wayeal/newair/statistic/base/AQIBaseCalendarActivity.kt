package com.wayeal.newair.statistic.base

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.monitor.bean.*
import com.wayeal.newair.statistic.bean.AQICalendarDayInfo
import com.wayeal.newair.statistic.bean.AQIDayDataWraper
import com.wayeal.newair.statistic.bean.CalenderDayItem
import com.wayeal.newair.statistic.bean.ComponentParam
import kotlinx.android.synthetic.main.activity_air_quality_calender.*
import kotlinx.android.synthetic.main.common_item_with_arrow.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

abstract class AQIBaseCalendarActivity : AQIBaseUICalendarActivity() {

    private val TAG = "AQIBaseCalendar"

    protected var checkElems = ArrayList<String>()              //已经选择的监测项
    protected var mComponentList = ArrayList<ComponentBean>()   //所有监测项目的信息

    protected val mDataList:MutableList<AQICalendarDayInfo> = ArrayList()       //日历数据,真实得数据

    override fun initData() {
        getAirStationTree()
    }

    /**
     * 获取站点信息
     * */
    private fun getAirStationTree() {
        val params = HashMap<String, String>()
        params[AirConstants.TYPE] = ""
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_AIR_STATION_TREE)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<TestPoint>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<TestPoint>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            if (d?.data != null) {
                                Log.i("AQIBaseCalendarActivity", "onSuccess: getAirStationTree")
                                mAirStations = d.data
                                mSelectProvince = mAirStations[0]
                                mSelectCity = mSelectProvince!!.children?.get(0)
                                mSelectCounty = mSelectCity!!.children?.get(0)
                                mSelectStation = mSelectCounty!!.children?.get(0)
                                station.value.text = mSelectStation?.title
                                getReportConfigItem()
                            } else {
                                Toast.makeText(this@AQIBaseCalendarActivity, "站点信息获取失败", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i("AQIBaseCalendarActivity", "onFailure: getAirStationTree")
                    }
                })
    }

    /**
     * 获取配置信息
     * */
    private fun getReportConfigItem() {
        val params = HashMap<String, String>()
        val conditionParams = HashMap<String, Any>()
        conditionParams["Type"] = "air-GIS"
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_REPORT_CONFIG)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<ConfigItem>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<ConfigItem>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            Log.i("AQIBaseCalendarActivity", "onSuccess: getReportConfigItem")
                            if (d?.data != null) {
                                checkElems = d.data[0].CheckElems as ArrayList<String>
                                getComponentInfo()
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i("AQIBaseCalendarActivity", "onFailure: getReportConfigItem")
                    }
                })
    }

    /**
     * 获取组件信息
     * */
    private fun getComponentInfo() {
        Log.i("AQIBaseCalendarActivity", "getComponentInfo: ")
        val params = HashMap<String, String>()
        val conditionParams = HashMap<String, Any>()
        conditionParams["Type"] = "air"
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_COMPONENT_INFO)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<ComponentBean>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<ComponentBean>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            Log.i("AQIBaseCalendarActivity", "onSuccess: 获取组件成功 整合出污染物项目")
                            for (component in d?.data!!) {
                                for (check in checkElems) {
                                    if (check == component.ComponentID) {
                                        mComponentList.add(component)
                                    }
                                }
                            }
                            Log.i("AQIBaseCalendarActivity", "onSuccess: " +
                                    "组件 size = ${mComponentList.size} 获取日历数据")
                            generateDataReport()
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i("AQIBaseCalendarActivity", "onFailure: 组件信息获取失败")
                    }
                })
    }

    /**
     * 获取月度日历数据
     * */
    protected fun generateDataReport() {
        Log.i("AQIBaseCalendarActivity", "generateDataReport: ")
        val params = HashMap<String, String>()
        val conditionParams = ArrayList<String>()
        conditionParams.add("${mSelectProvince?.key}/${mSelectCity?.key}/${mSelectCounty?.key}/${mSelectStation?.key}")
        //条件格式是数组
        params[AirConstants.CONDITION] = Gson().toJson(conditionParams)
        val timeParams = HashMap<String, Any>()
        //时间格式是月份的第一天
        timeParams[AirConstants.STARTIME] = "${SimpleDateFormat("yyyy/MM").format(mDate)}/01 00:00:00"
        params[AirConstants.TIME] = AirUtils.mapTojson(timeParams)
        params[AirConstants.TYPE] = AirConstants.AIR_MONTH_CALENDAR
        val componentParams = ArrayList<ComponentParam>()
        if (mComponentList.size == 0) {
            Log.i("AQIBaseCalendarActivity", "generateDataReport: 组件信息不足 无法查询月度日历数据")
            return
        }
        for (component in mComponentList) {
            componentParams.add(ComponentParam(ComponentID = component.ComponentID, Type = AirConstants.AIR_MONTH_CALENDAR))
        }
        //需要查询的监测项
        params[AirConstants.COMPONENTS] = Gson().toJson(componentParams)
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GENERATE_DATA_REPORT)
                        , params
                        , object : GsonResponseHandler<WanResponse<AQIDayDataWraper>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<AQIDayDataWraper>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            if (d?.data != null) {
                                handleCalenderData(d.data)
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 日历获取失败")
                    }
                })
    }

    /**
     * 处理返回的日历数据
     * */
    private fun handleCalenderData(dataWraper: AQIDayDataWraper) {
        Log.i("AQIBaseCalendarActivity", "handleCalenderData: " +
                "日历数据size = ${dataWraper.externalData.size}")
        mDataList.clear()
        val externalData = dataWraper.externalData
        for (index in externalData.indices) {
            Log.i("AQIBaseCalendarActivity", "handleCalenderData: dayInfo = ${externalData[index]}")
            val componentList = ArrayList<ComponentBean>()
            //获取并且赋值AQI信息的监测项值
            for (component in mComponentList) {
                if (externalData[index].get(component.ComponentID) != null) {
                    component.Value = externalData[index].get(component.ComponentID).asString
                    componentList.add(component)
                }
            }
            Log.i("AQIBaseCalendarActivity", "handleCalenderData: 处理后list = $componentList")
            //AQI的值
            val value: String? = if (externalData[index].get("value") != null) {
                externalData[index].get("value").asString
            } else {
                ""
            }
            //主要污染物
            val primaryPollutant: String? = if (externalData[index].get("PrimaryPollutant") != null) {
                externalData[index].get("PrimaryPollutant").asString
            } else {
                ""
            }
            val tmpList = ArrayList<ComponentBean>()
            tmpList.addAll(componentList)
            Log.i(TAG, "handleCalenderData: tmplist = ${tmpList}")
            val aqiCalendarDayInfo = AQICalendarDayInfo(color = externalData[index].get("color").asString
                    , name = externalData[index].get("name").asString
                    , time = externalData[index].get("time").asString
                    , value = value
                    , PrimaryPollutant = primaryPollutant
                    , componentList = tmpList
                    , componentListString = Gson().toJson(tmpList))
            //包含了所有信息的日数据
            Log.i(TAG, "handleCalenderData: aqiinfo = ${aqiCalendarDayInfo.componentList}")
            mDataList.add(aqiCalendarDayInfo)
        }
        Log.i(TAG, "refreshCalendar:1 第一个 = ${mDataList[0].componentListString}")
        Log.i(TAG, "refreshCalendar:1 第二个 = ${mDataList[1].componentListString}")
        if (mDataList.size > 0) {
            Log.i("AQIBaseCalendarActivity", "handleCalenderData: size = ${mDataList.size}")
            refreshCalendar()
        }
    }

    /**
     * 获取的AQI值赋值到日历上
     * */
    @SuppressLint("SetTextI18n")
    private fun refreshCalendar() {
        Log.i(TAG, "refreshCalendar: 第一个 = ${mDataList[0].componentListString}")
        Log.i(TAG, "refreshCalendar: 第二个 = ${mDataList[1].componentListString}")
        mSelectDay = AirUtils.Date2Calendar(mDate).get(Calendar.DAY_OF_MONTH).toString()
        showDate()
        //设置选中的天
        mCalendarAdapter?.setSelectDay(mSelectDay)
        calculateDays()
        //日历数据排列
        mCalendarDataList.clear()
        //添加头部不必显示的日历天数据
        if (dayOfWeek != 1) {
            for (i in 0 until dayOfWeek - 1) {
                mCalendarDataList.add(CalenderDayItem(isShow = false))
            }
        }
        //添加中间AQI日历数据
        Log.i(TAG, "refreshCalendar: mdata size = ${mDataList.size}")
        for (index in mDataList.indices) {
            Log.i(TAG, "refreshCalendar: index = $index")
            mCalendarDataList.add(CalenderDayItem(isShow = true, dayInfo = mDataList[index]))
        }
        //添加尾部不必显示的日历天数据
        for (j in dayOfWeek + daysOfMonth - 1 until 41) {
            mCalendarDataList.add(CalenderDayItem(isShow = false))
        }
        mCalendarAdapter?.setData(mCalendarDataList)
        //显示选中的那天的AQI信息
        for (day in mCalendarDataList) {
            if (day.dayInfo?.time.equals(mSelectDay)) {
                showAqiInfo(day.dayInfo)
            }
        }
    }

    /**
     * 显示AQI信息，其中颜色和AQI值都是固定的，污染物和其他内容会变化
     * */
    private fun showAqiInfo(dayInfo: AQICalendarDayInfo?) {
        //AQI颜色
        if (dayInfo?.color.isNullOrEmpty()) {
            aqiBg.setBackgroundColor(AirUtils.getRgbInt(AirConstants.NO_DATA_COLOR))
        } else {
            aqiBg.setBackgroundColor(AirUtils.getRgbInt(dayInfo?.color))
        }
        //AQI数据
        val aqiText: String?
        aqiText = if (dayInfo?.value.isNullOrEmpty()) {
            "AQI: 无数据"
        } else {
            when (dayInfo?.value?.toInt()) {
                in 0..50 -> "AQI: 优"
                in 51..100 -> "AQI: 良"
                in 101..150 -> "AQI: 轻度污染"
                in 151..200 -> "AQI: 中度污染"
                in 201..300 -> "AQI: 重度污染"
                in 301..9999999 -> "AQI: 严重污染"
                else -> "AQI: 无数据"
            }
        }
        aqi.text = aqiText
        aqiValue.text = dayInfo?.value
        //污染物
        val pollutionText = "首要污染物："+ dayInfo?.PrimaryPollutant
        mainPollution.text = pollutionText
        //污染物类型
        Log.i(TAG, "showAqiInfo: componentListString = ${dayInfo?.componentListString}")
        if (dayInfo?.componentListString == null) {
            val tmpList = ArrayList<ComponentBean>()
            for (param in checkElems) {
                tmpList.add(ComponentBean(ComponentID = ""
                        , Description = param
                        , Unit = ""
                        , CreateTime = ""
                        , Value = ""
                        , Type = ""))
            }
            mPollutionAdapter?.setData(tmpList)
        } else {
//            mPollutionAdapter?.setData(dayInfo?.componentList!! as ArrayList<ComponentBean>)
            val tmpList = ArrayList<ComponentBean>()
            val listString = dayInfo.componentListString
            val list = Gson().fromJson(listString,Array<ComponentBean>::class.java)
            for (item in list){
                tmpList.add(item)
            }
            mPollutionAdapter?.setData(tmpList)
        }
    }

    protected fun showAqiInfo(position: Int) {
        val aqiData = mCalendarDataList[position]
        val dayInfo = aqiData.dayInfo
        showAqiInfo(dayInfo)
    }
}