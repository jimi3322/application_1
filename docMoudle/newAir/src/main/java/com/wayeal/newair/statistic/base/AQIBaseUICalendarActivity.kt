package com.wayeal.newair.statistic.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.wayeal.newair.R
import com.wayeal.newair.base.BaseActivity
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.SpaceItemDecoration
import com.wayeal.newair.common.dialog.ChoosePointDialog
import com.wayeal.newair.common.view.ChooseView
import com.wayeal.newair.monitor.bean.CityPoint
import com.wayeal.newair.monitor.bean.CountyPoint
import com.wayeal.newair.monitor.bean.TestPoint
import com.wayeal.newair.monitor.bean.TestPointDetail
import com.wayeal.newair.statistic.adapter.AqiCalendarAdapter
import com.wayeal.newair.statistic.adapter.AqiPollutionAdapter
import com.wayeal.newair.statistic.bean.AQICalendarDayInfo
import com.wayeal.newair.statistic.bean.CalenderDayItem
import kotlinx.android.synthetic.main.activity_air_quality_calender.*
import kotlinx.android.synthetic.main.activity_air_quality_calender.station
import kotlinx.android.synthetic.main.common_item_with_arrow.view.*
import java.util.*
import kotlin.collections.ArrayList

abstract class AQIBaseUICalendarActivity:BaseActivity() {

    private val TAG = "AQIBaseUICalendar"

    protected var mAirStations: ArrayList<TestPoint> = ArrayList()      //站点
    protected var mSelectStation: TestPointDetail? = null               //选择的监测站点
    protected var mSelectCounty: CountyPoint? = null                    //选择的区
    protected var mSelectCity: CityPoint? = null                        //选择的城市
    protected var mSelectProvince: TestPoint? = null                    //选择的省

    protected lateinit var mDate: Date                                  //选择的时间
    private var mSelectYear = 0                                         //选择的年份
    private var mSelectMonth = 0                                        //选择的月份
    protected var dayOfWeek = 7                                         //本月第一天是周几
    protected var daysOfMonth = 0                                       //本月一共多少天
    protected var mSelectDay: String = "0"                              //默认选择得天数，这里1~31

    protected var mCalendarAdapter: AqiCalendarAdapter? = null
    protected var mCalendarDataList: ArrayList<CalenderDayItem> = ArrayList(42)  //  日历数据 必定是42个
    protected var mPollutionAdapter: AqiPollutionAdapter? = null

    override fun initView() {
        initStationView()
        initTimeView()
        initRecyclerView()
        initPollutionRecyclerView()
    }

    /**
     * 监测站点
     * */
    private fun initStationView(){
        //监测站点
        var stationDialog: ChoosePointDialog? = null
        station.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                if (mAirStations.isEmpty()){
                    Toast.makeText(this@AQIBaseUICalendarActivity, "站点信息还没有获取到 稍后再试", Toast.LENGTH_SHORT).show()
                    station.setExpand(false)
                }else{
                    stationDialog = ChoosePointDialog(this@AQIBaseUICalendarActivity
                            , getString(R.string.station)
                            , mAirStations,mSelectProvince,mSelectCity,mSelectCounty,mSelectStation
                            , object : ChoosePointDialog.ConfirmPointListener {
                        @SuppressLint("SetTextI18n")
                        override fun onConfirm(selectProvince: TestPoint?,
                                               selectCity: CityPoint?,
                                               selectCounty: CountyPoint?,
                                               selectStation: TestPointDetail?) {
                            mSelectProvince = selectProvince
                            mSelectCity = selectCity
                            mSelectStation = selectStation
                            mSelectCounty = selectCounty
                            if (mSelectStation == null){
                                station.value.text =
                                        "${mSelectCounty?.title}/全部"
                            }else{
                                station.value.text =
                                        "${mSelectCounty?.title}/${mSelectStation?.title}"
                            }
                            station.setExpand(false)
                        }
                        override fun onCancel() {

                        }
                    })
                    stationDialog?.show()
                }
            }

            override fun onShrink() {
                stationDialog?.dismiss()
            }
        })
    }

    /**
     * 选择月份
     * */
    private fun initTimeView(){
        mDate = Date(System.currentTimeMillis())
        time.setValue(AirUtils.getMonthTime(mDate))
        val timeDialog = TimePickerBuilder(this, OnTimeSelectListener {
            date, _ ->
            time.setExpand(false)
            mDate = date
            time.setValue(AirUtils.getMonthTime(date))
        }).setType(booleanArrayOf(true, true, true, false, false, false))
                .setItemVisibleCount(5)
                .setLineSpacingMultiplier(5.0f)
                .isAlphaGradient(true)
                .setContentTextSize(18)
                .setTitleSize(18)
                .setOutSideCancelable(true)
                .isCyclic(true)
                .setSubmitColor(Color.parseColor("#40C1B0"))
                .setCancelColor(Color.parseColor("#333333"))
                .setTitleBgColor(Color.parseColor("#ffffff"))
                .setBgColor(Color.parseColor("#ffffff"))
                .setTitleSize(30)
                .setTitleText("Title")
                .setTitleColor(Color.parseColor("#ffffff"))
                .setTextColorCenter(Color.parseColor("#40C1B0"))
                .setDate(AirUtils.Date2Calendar(mDate))
                .build()
        time.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onShrink() {
                timeDialog.dismiss()
            }

            override fun onExpand() {
                timeDialog.show()
            }
        })
    }

    /**
     * 日历组件初始化
     * */
    private fun initRecyclerView(){
        val gridManager = GridLayoutManager(this,7,GridLayoutManager.VERTICAL,false)
        calender.layoutManager = gridManager
        mCalendarAdapter = AqiCalendarAdapter(this)
        calender.adapter = mCalendarAdapter
        calender.addItemDecoration(SpaceItemDecoration(top = 10,bottom = 10,left = 10,right = 10))
        initDefaultCalendar()
    }

    /**
     * 污染物列表
     * */
    private fun initPollutionRecyclerView(){
        val linearLayoutManager = LinearLayoutManager(this)
        pollutionValue.layoutManager = linearLayoutManager
        mPollutionAdapter = AqiPollutionAdapter(this)
        pollutionValue.adapter = mPollutionAdapter
        pollutionValue.addItemDecoration(SpaceItemDecoration(bottom = 10))
    }

    /**
     * 赋值日历默认值
     * */
    private fun initDefaultCalendar(){
        calculateDays()
        val calendarList = ArrayList<CalenderDayItem>()
        if (dayOfWeek != 1){
            for (i in 0 until  dayOfWeek-1){
                calendarList.add(CalenderDayItem(isShow = false))
            }
        }
        for (i in dayOfWeek - 1 until dayOfWeek + daysOfMonth - 1){
            calendarList.add(CalenderDayItem(isShow = true))
        }
        for (i in dayOfWeek + daysOfMonth - 1 until 41){
            calendarList.add(CalenderDayItem(isShow = false))
        }
        mCalendarAdapter?.setData(calendarList)
    }

    /**
     * 计算日期
     * */
    protected fun calculateDays(){
        mSelectYear = AirUtils.Date2Calendar(mDate).get(Calendar.YEAR)
        mSelectMonth = AirUtils.Date2Calendar(mDate).get(Calendar.MONTH) + 1
        Log.i(TAG, "calculateDays: mSelectYear = $mSelectYear  mSelectMonth = $mSelectMonth")
        dayOfWeek = AirUtils.getMonthFirstDay(mSelectYear,mSelectMonth)
        Log.i(TAG, "initRecyclerView: 本月第一天是周${dayOfWeek}")
        daysOfMonth = AirUtils.getMonthOfDay(mSelectYear,mSelectMonth)
        Log.i(TAG, "initDefaultCalendar: 本月一共有${daysOfMonth}天")
    }

    /**
     * 展示日历上的大的日期和下面描述信息的日期
     * */
    @SuppressLint("SetTextI18n")
    protected fun showDate(){
        day.text = mSelectDay
        month.text = "${AirUtils.Date2Calendar(mDate).get(Calendar.MONTH) + 1}月"
        val year = AirUtils.Date2Calendar(mDate).get(Calendar.YEAR)
        val month = AirUtils.Date2Calendar(mDate).get(Calendar.MONTH) + 1
        aqiTime.text = "${year}-${month}-${mSelectDay}"
    }

}