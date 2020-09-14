package com.wayeal.newair.statistic

import android.annotation.SuppressLint
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.SpaceItemDecoration
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.monitor.bean.*
import com.wayeal.newair.statistic.adapter.AqiCalendarAdapter
import com.wayeal.newair.statistic.adapter.AqiStandardAdapter
import com.wayeal.newair.statistic.base.AQIBaseCalendarActivity
import kotlinx.android.synthetic.main.activity_air_quality_calender.*
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

const val TAG = "AQICalender"

class AirQualityCalenderActivity: AQIBaseCalendarActivity() {

    private var mStandardList = ArrayList<StandardInfo>()               //AQI标准信息，用来构建图例
    private var mStandardAdapter: AqiStandardAdapter? = null            //AQI图例适配器

    override fun getLayoutId(): Int {
        return R.layout.activity_air_quality_calender
    }

    override fun initView() {
        super.initView()
        getCalender.setOnClickListener {
            generateDataReport()
        }
        //日历组件设置点击事件
        mCalendarAdapter?.setOnItemClickListener(object : AqiCalendarAdapter.OnItemClickListener {
            @SuppressLint("SetTextI18n")
            override fun setOnclickItem(position: Int) {
                Log.i(TAG, "setOnclickItem: pos = $position")
                //选中那一天
                mSelectDay = mCalendarDataList[position].dayInfo?.time.toString()
                mCalendarAdapter?.setSelectDay(mSelectDay)
                //显示日期
                showDate()
                //显示AQI信息
                showAqiInfo(position)
            }
        })
    }

    override fun initToolbar() {
        toolbar.title.text = resources.getString(R.string.air_quality_calender)
        toolbar.back.visibility = View.VISIBLE
        toolbar.back.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
        super.initData()
        getStandardInfo()
    }

    /**
     * 获取AQI标准信息
     * */
    private fun getStandardInfo(){
        val params = HashMap<String, String>()
        val conditionParams = HashMap<String, Any>()
        conditionParams[AirConstants.SETTING_TYPE] = "air-quality"
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_STANDARD_INFO)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<StandardInfo>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<StandardInfo>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            Log.i(TAG, "onSuccess: getStandardInfo")
                            if (d?.data != null){
                                mStandardList = d.data
                                buildStandardInfo(mStandardList)
                            }else{
                                Toast.makeText(this@AirQualityCalenderActivity, "AQI信息获取失败", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: getStandardInfo")
                    }
                })
    }

    /**
     * 构建空气标准信息
     * */
    private fun buildStandardInfo(standardList: ArrayList<StandardInfo>){
        Log.i(TAG, "buildStandardInfo: ")
        val tmpList = ArrayList<StandardInfo>()
        tmpList.clear()
        //添加无数据的图例
        tmpList.add(StandardInfo(StandardId = "0000"
                ,Level = "0级"
                ,Priority = "0"
                ,StatusName = "无数据"
                ,RangeHigh = ""
                ,RangeLow = ""
                ,Effect = ""
                ,Measures = ""
                ,Color = "rgb(170,170,170)"
                ,SettingType = ""
                ,CreateTime = ""))
        tmpList.addAll(standardList)
        //AQI图例适配器
        mStandardAdapter = AqiStandardAdapter(this)
        val gridLayoutManager = GridLayoutManager(this,tmpList.size)
        standardInfo.layoutManager = gridLayoutManager
        standardInfo.addItemDecoration(SpaceItemDecoration(left = AirUtils.dip2px(3f),right = AirUtils.dip2px(3f)))
        standardInfo.adapter = mStandardAdapter
        mStandardAdapter?.setData(tmpList)
    }

}