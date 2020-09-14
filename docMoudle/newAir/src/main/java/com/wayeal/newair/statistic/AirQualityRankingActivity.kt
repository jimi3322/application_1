package com.wayeal.newair.statistic

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.google.gson.Gson
import com.wayeal.newair.R
import com.wayeal.newair.base.BaseActivity
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.common.dialog.ChoosePointDialog
import com.wayeal.newair.common.dialog.CommonListDialog
import com.wayeal.newair.common.view.ChooseView
import com.wayeal.newair.monitor.bean.*
import com.wayeal.newair.statistic.adapter.AqiRankingAdapter
import com.wayeal.newair.statistic.bean.AQIRankingData
import com.wayeal.newair.statistic.bean.AQIRankingDataWraper
import com.wayeal.newair.statistic.bean.ComponentParam
import kotlinx.android.synthetic.main.activity_air_quality_ranking.*
import kotlinx.android.synthetic.main.activity_air_quality_ranking.loading
import kotlinx.android.synthetic.main.activity_air_quality_ranking.refreshLayout
import kotlinx.android.synthetic.main.activity_air_quality_ranking.station
import kotlinx.android.synthetic.main.activity_air_quality_ranking.time
import kotlinx.android.synthetic.main.activity_air_quality_ranking.toolbar
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.common_item_with_arrow.view.*
import kotlinx.android.synthetic.main.dialog_list.view.*
import kotlinx.android.synthetic.main.dialog_list.view.title
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AirQualityRankingActivity : BaseActivity() {

    private val TAG = "AirQualityRank"

    private var mRankingType: String? = null    //排行类型
    private var mTime: Date? = null                                                          //时间
    private var mDate: Date? = null                                                          //日期

    private var mSelectStation: TestPointDetail? = null       //选择的监测站点
    private var mSelectCounty: CountyPoint? = null      //选择的区
    private var mSelectCity: CityPoint? = null          //选择的城市
    private var mSelectProvince: TestPoint? = null      //选择的省

    private var mProvinces: ArrayList<TestPoint> = ArrayList()        //获取的站点信息

    private var mAdapter: AqiRankingAdapter? = null

    private var checkElems = ArrayList<String>()        //选择的监测项目
    private val mDataList: ArrayList<AQIRankingData> = ArrayList()          //AQI

    override fun getLayoutId(): Int {
        return R.layout.activity_air_quality_ranking
    }

    override fun initView() {
        initItemView()
        initRecyclerView()
        initRefreshLayout()
    }

    override fun initIntentData() {

    }

    override fun initToolbar() {
        toolbar.title.text = resources.getString(R.string.air_quality_rank)
        toolbar.back.visibility = View.VISIBLE
        toolbar.back.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
        Log.i(TAG, "initData: getStation")
        val params = HashMap<String, String>()
        params[AirConstants.TYPE] = ""
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_AIR_STATION_TREE)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<TestPoint>>>() {
                    @SuppressLint("SetTextI18n")
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<TestPoint>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            Log.i(TAG, "onSuccess: 获取监测站点信息成功")
                            mProvinces = d?.data!!
                            mSelectProvince = mProvinces[0]
                            mSelectCity = mSelectProvince?.children!![0]
                            mSelectCounty = mSelectCity?.children!![0]
                            station.value.text = "${mSelectProvince?.title}/${mSelectCity?.title}"
                            getReportConfigItem()
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 站点信息获取失败")

                    }
                })
    }

    /**
     * 配置信息
     * */
    private fun getReportConfigItem() {
        Log.i(TAG, "getReportConfigItem: ")
        val params = HashMap<String, String>()
        val conditionParams = HashMap<String, Any>()
        conditionParams["Type"] = "air-hourRank"
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_REPORT_CONFIG)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<ConfigItem>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<ConfigItem>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            Log.i(TAG, "onSuccess: 获取配置成功 再获取排行数据")
                            if (d?.data != null) {
                                checkElems = d.data[0].CheckElems as ArrayList<String>
                                getRankingData()
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 配置信息获取失败")
                    }
                })
    }

    /**
     * 获取AQI排行数据
     * */
    private fun getRankingData() {
        Log.i(TAG, "getRankingData: ")
        val params = HashMap<String, String>()
        //选择的区域
        val conditionParams = ArrayList<String>()
        conditionParams.add("${mSelectProvince?.key}/${mSelectCity?.key}")
        params[AirConstants.CONDITION] = Gson().toJson(conditionParams)
        //时间
        val timeParams = HashMap<String, Any>()
        when(mRankingType){
            resources.getString(R.string.real_time_ranking) -> {
                timeParams[AirConstants.STARTIME] = AirUtils.getRealTime(mTime!!).toString()
            }
            resources.getString(R.string.day_ranking) -> {
                timeParams[AirConstants.STARTIME] = AirUtils.getDayTime(mDate!!).toString()
            }
        }
        params[AirConstants.TIME] = AirUtils.mapTojson(timeParams)
        //type
        params[AirConstants.TYPE] = AirConstants.AIR_HOUR_RANK
        //components
        val componentParams = ArrayList<ComponentParam>()
        if (checkElems.size == 0) {
            Log.i(TAG, "generateDataReport: 组件信息不足 无法查询月度日历数据")
            return
        }
        for (component in checkElems) {
            componentParams.add(ComponentParam(ComponentID = component, Type = AirConstants.AIR))
        }
        params[AirConstants.COMPONENTS] = Gson().toJson(componentParams)
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GENERATE_DATA_REPORT)
                        , params
                        , object : GsonResponseHandler<WanResponse<AQIRankingDataWraper>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<AQIRankingDataWraper>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            Log.i(TAG, "onSuccess: 获取ranking信息成功 data = ${d?.data?.data}")
                            Log.i(TAG, "onSuccess: 获取ranking信息成功 color = ${d?.data?.Color}")
                            handleRankingData(d?.data!!)
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 获取到ranking信息失败")
                    }
                })
    }


    /**
     * 处理返回的ranking信息
     * */
    private fun handleRankingData(dataWraper: AQIRankingDataWraper) {
        val infoList = dataWraper.data
        val keys = infoList.keySet()
        for (key in keys) {
            Log.i(TAG, "handleRankingData: key = $key")
            if (key != "1") {
                val info = infoList.get(key).asJsonObject
                mDataList.add(AQIRankingData(Rank = info.get("A").asString,
                        Name = info.get("B").asString,
                        AQI = info.get("C").asString,
                        QualityStatus = info.get("D").asString,
                        PrimaryPollutant = info.get("H").asString))
            }
        }
        if (mDataList.size == 0) {
            Log.i(TAG, "handleRankingData: 数据异常")
        } else {
            for (ranking in mDataList) {
                Log.i(TAG, "handleRankingData: rank = ${ranking.Rank}")
            }
            loading.showContent()
            refreshLayout.finishRefresh()
            mAdapter?.setData(mDataList)
        }
    }


    /**
     * 初始化排行类型、时间和区域
     * */
    private fun initItemView() {
        //排行类型
        mRankingType = this.resources.getString(R.string.real_time_ranking)
        rankingType.setValue(resources.getString(R.string.real_time_ranking))
        val rankingDialog = CommonListDialog(this
                , getString(R.string.ranking_type)
                , AirUtils.getStringList(R.array.RankingType)
                , object : CommonListDialog.onItemClickListener {
            override fun onResult(pos: Int, result: String) {
                rankingType.setValue(result)
                rankingType.setExpand(false)
                mRankingType = result
                if (pos == 0) {
                    //实时排行
                    time.isEnabled = false
                    time.isClickable = false
                    time.hideArrow()
                    mTime = Date()
                    time.setValue(AirUtils.getRealTime(mTime!!))
                } else if (pos == 1) {
                    //日排行
                    time.isEnabled = true
                    time.isClickable = true
                    time.showArrow()
                    showTimeDialog()
                }
            }
        })
        rankingType.setExpandListener(object : ChooseView.OnChooseClickListener {
            override fun onShrink() {
                rankingDialog.dismiss()
            }

            override fun onExpand() {
                Log.i(TAG, "onExpand: ")
                rankingDialog.show()
            }
        })

        //时间
        when (mRankingType) {
            getString(R.string.real_time_ranking) -> {
                time.hideArrow()
                time.isClickable = false
                mTime = Date()
                time.setValue(AirUtils.getRealTime(mTime!!))
            }
            getString(R.string.day_ranking) -> {
                time.showArrow()
                time.isClickable = true
                showTimeDialog()
            }
        }

        //监测区域
        station.setValue("-/-")
        var stationDialog: ChoosePointDialog? = null
        station.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                if (mProvinces.size == 0){
                    Toast.makeText(this@AirQualityRankingActivity, "站点信息还没有获取到 稍后再试", Toast.LENGTH_SHORT).show()
                    station.setExpand(false)
                }else{
                    stationDialog = ChoosePointDialog(this@AirQualityRankingActivity
                            , getString(R.string.station)
                            , mProvinces,mSelectProvince,mSelectCity,mSelectCounty,mSelectStation
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
     * 点击选择日期 进行日排行
     * */
    private fun showTimeDialog(){
        mDate = Date(System.currentTimeMillis())
        time.setValue(AirUtils.getDayTime(mDate!!))
        val dateDialog = TimePickerBuilder(this, OnTimeSelectListener {
            date, _ ->
            time.setExpand(false)
            mDate = date
            time.setValue(AirUtils.getDayTime(date))
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
                .setDate(AirUtils.Date2Calendar(mDate!!))
                .build()
        time.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onShrink() {
                dateDialog.dismiss()
            }

            override fun onExpand() {
                dateDialog.show()
            }
        })
    }

    /**
     * recyclerView初始化
     * */
    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        ranking.layoutManager = linearLayoutManager
        mAdapter = AqiRankingAdapter(this)
        ranking.adapter = mAdapter
    }

    /**
     * loading初始化
     * */
    private fun initRefreshLayout() {
        refreshLayout.setEnableLoadMore(true)
        refreshLayout.setOnRefreshListener {
            getRankingData()
        }
        loading.setRetryListener {
            loading.showContent()
            refreshLayout.autoRefresh()
        }
    }

}