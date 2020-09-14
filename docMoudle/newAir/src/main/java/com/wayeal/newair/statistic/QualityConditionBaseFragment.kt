package com.wayeal.newair.statistic

import android.graphics.Color
import android.util.Log
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.wayeal.newair.base.BaseFragment
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.common.view.ChooseView
import com.wayeal.newair.monitor.bean.*
import kotlinx.android.synthetic.main.common_item_with_arrow.view.*
import kotlinx.android.synthetic.main.fragment_quality_condition_statistic.*
import java.util.*

abstract class QualityConditionBaseFragment:BaseFragment() {

    protected lateinit var stDate: Date       //开始时间日期
    protected lateinit var edDate: Date       //结束时间日期

    protected var mAirStations: ArrayList<TestPoint> = ArrayList()     //空气站数据
    protected var mSelectStation: TestPointDetail? = null       //选择的监测站点
    protected var mSelectCounty: CountyPoint? = null      //选择的区
    protected var mSelectCity: CityPoint? = null          //选择的城市
    protected var mSelectProvince: TestPoint? = null      //选择的省

    protected var checkElems = ArrayList<String>()        //选择的监测项目

    override fun initView() {
        stDate = Date(System.currentTimeMillis() - 7 * 24 * 3600 * 1000)
        startDate.setValue(AirUtils.getDate(stDate))
        val stDateDialog = TimePickerBuilder(mActivity, OnTimeSelectListener {
            date, _ ->
            startDate.setExpand(false)
            stDate = date
            startDate.setValue(AirUtils.getDayTime(date))
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
                .setDate(AirUtils.Date2Calendar(stDate))
                .build()
        startDate.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onShrink() {
                stDateDialog.dismiss()
            }

            override fun onExpand() {
                stDateDialog.show()
            }
        })

        //设置结束时间日期
        edDate = Date(System.currentTimeMillis() - 24 * 3600 * 1000)
        edDate = Date(System.currentTimeMillis())
        endDate.setValue(AirUtils.getDate(edDate))
        val edDateDialog = TimePickerBuilder(mActivity, OnTimeSelectListener {
            date, _ ->
            endDate.setExpand(false)
            edDate = date
            endDate.setValue(AirUtils.getDayTime(date))
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
                .setDate(AirUtils.Date2Calendar(edDate))
                .build()
        endDate.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onShrink() {
                edDateDialog.dismiss()
            }

            override fun onExpand() {
                edDateDialog.show()
            }
        })
    }

    override fun initData() {
        getAirStationTree()
        getReportConfigItem()
    }

    private fun getAirStationTree(){
        val params = HashMap<String, String>()
        params[AirConstants.TYPE] = ""
        OkHttpUtils.getInstance()
                .post(mActivity, HttpUtil.getAirServiceAddress(AirConstants.GET_AIR_STATION_TREE)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<TestPoint>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<TestPoint>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            if (d?.data != null) {
                                Log.i(TAG, "onSuccess: 获取站点树信息成功")
                                mAirStations = d.data
                                mSelectProvince = mAirStations[0]
                                mSelectCity = mSelectProvince!!.children?.get(0)
                                mSelectCounty = mSelectCity!!.children?.get(0)
                                mSelectStation = mSelectCounty!!.children?.get(0)
                                station.value.text = mSelectStation?.title
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 站点信息获取失败")
                    }
                })
    }

    private fun getReportConfigItem(){
        Log.i(TAG, "getReportConfigItem: ")
        val params = HashMap<String, String>()
        val conditionParams = HashMap<String, Any>()
        conditionParams["Type"] = "air-qualityStatus"
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        OkHttpUtils.getInstance()
                .post(mActivity, HttpUtil.getAirServiceAddress(AirConstants.GET_REPORT_CONFIG)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<ConfigItem>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<ConfigItem>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            Log.i(TAG, "onSuccess: 获取配置成功 ")
                            if (d?.data != null){
                                checkElems = d.data[0].CheckElems as ArrayList<String>
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 配置信息获取失败")
                    }
                })
    }

}