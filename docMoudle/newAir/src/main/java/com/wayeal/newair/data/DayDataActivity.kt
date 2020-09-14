package com.wayeal.newair.data

import HistoricalDatas
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.*
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.wayeal.newair.R
import com.wayeal.newair.base.BaseActivity
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.BaseChooseItemType
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.common.dialog.ChoosePointDialog
import com.wayeal.newair.common.dialog.MulipleDialog
import com.wayeal.newair.common.view.ChooseView
import com.wayeal.newair.data.adapter.HeaderAdapter
import com.wayeal.newair.data.adapter.HistoricalDataAdapter
import com.wayeal.newair.data.bean.TabelHeaderData
import com.wayeal.newair.monitor.bean.*
import kotlinx.android.synthetic.main.activity_historical_data.*
import kotlinx.android.synthetic.main.activity_historical_data.loading
import kotlinx.android.synthetic.main.activity_historical_data.refreshLayout
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.common_blue_tablayout.view.title
import kotlinx.android.synthetic.main.common_item_with_arrow.view.*
import kotlinx.android.synthetic.main.fragment_statistic.toolbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap



@Suppress("NAME_SHADOWING")
class DayDataActivity : BaseActivity(){
    private val mContext: Context? = this
    private var mSelectStation: TestPointDetail? = null       //选择的监测站点
    private var mSelectCounty: CountyPoint? = null      //选择的区
    private var mSelectCity: CityPoint? = null          //选择的城市
    private var mSelectProvince: TestPoint? = null      //选择的省
    private var mProvinces:ArrayList<TestPoint> = ArrayList()        //获取的站点信息
    private var mMonitorItems:ArrayList<TabelHeaderData> = ArrayList()     //监测项目
    private var mselectMonitorItems:ArrayList<TabelHeaderData> = ArrayList()     //已选的监测项目
    private var mselectMonitorHeader:ArrayList<String> = ArrayList()     //已选的监测项目的表头
    private var mselectMonitorComponentID:ArrayList<String> = ArrayList()     //已选的监测项目的ID
    private var mDataList: ArrayList<HistoricalDatas>? = ArrayList()
    private var mCurrentPageNumber = 1
    private var mAdapter: HistoricalDataAdapter? = null
    private var mHeaderAdapter: HeaderAdapter? = null
    private var mCounts: ArrayList<Int> = ArrayList()          //监测弹窗选择的监测项

    private var startDate:String = ""
    private var endDate:String = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_day_data
    }

    override fun initView() {
        initDateTime()
        initPoint()
        initItemView()
    }

    /**
     * 选择Item的View
     **/
    private fun initItemView(){
        //开始时间
        start_date.value.text = startDate
        start_date.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                TimePickerBuilder(this@DayDataActivity,OnTimeSelectListener {
                    date, _ ->
                    Log.i(TAG, "onTimeSelect: ${AirUtils.getTime(date!!)}")
                    val date = AirUtils.getTime(date)
                    start_date.value.text = date?.substring(0, date.indexOf(" "))
                    start_date.setExpand(false)
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
                        .addOnCancelClickListener{
                            start_date.setExpand(false)
                        }
                        .build().show()
            }
            override fun onShrink() {
                ///stationDialog?.dismiss()
            }
        })

        //结束时间
        end_date.value.text = endDate
        end_date.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                TimePickerBuilder(this@DayDataActivity, OnTimeSelectListener {
                    date, _ ->
                    Log.i(TAG, "onTimeSelect: ${AirUtils.getTime(date!!)}")
                    val date = AirUtils.getTime(date)
                    end_date.value.text = date?.substring(0, date.indexOf(" "))
                    end_date.setExpand(false)
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
                        .addOnCancelClickListener{
                            end_date.setExpand(false)
                        }
                        .build().show()
            }
            override fun onShrink() {
                ///stationDialog?.dismiss()
            }
        })

        //监测站点
        historyPoint.value.text = resources.getString(R.string.get_stations_loading)
        var stationDialog: ChoosePointDialog? = null
        historyPoint.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                if (mProvinces.size == 0){
                    Toast.makeText(this@DayDataActivity, "站点信息还没有获取到 稍后再试", Toast.LENGTH_SHORT).show()
                    historyPoint.setExpand(false)
                }else{
                    stationDialog = ChoosePointDialog(this@DayDataActivity
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
                                historyPoint.value.text =
                                        "${mSelectCounty?.title}/全部"
                            }else{
                                historyPoint.value.text =
                                        "${mSelectCounty?.title}/${mSelectStation?.title}"
                            }
                            loading.showContent()
                            //refreshLayout.autoRefresh()
                            historyPoint.setExpand(false)
                        }
                        override fun onCancel() {
                            historyPoint.setExpand(false)
                        }
                    })
                    stationDialog?.show()
                }
            }
            override fun onShrink() {
                stationDialog?.dismiss()
            }
        })

        //监测项目
        monitorItems.value.text = resources.getString(R.string.please_select_monitorItems)
        var monitorDialog: MulipleDialog? = null
        monitorItems.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                if (mMonitorItems.size == 0){
                    Toast.makeText(this@DayDataActivity, "监测信息还没有获取到 稍后再试", Toast.LENGTH_SHORT).show()
                    historyPoint.setExpand(false)
                }else{
                    monitorDialog = MulipleDialog(this@DayDataActivity
                            , getString(R.string.monitor_detail)
                            , mMonitorItems
                            , mCounts
                            ,4
                            , object : MulipleDialog.onItemClickListener {
                        override fun onResult(result: ArrayList<BaseChooseItemType>, mCounts:ArrayList<Int>) {
                            this@DayDataActivity.mCounts = mCounts
                            val monitorItemsView = StringBuffer()
                            mselectMonitorItems.clear()
                            mselectMonitorHeader.clear()
                            mselectMonitorComponentID.clear()
                            mselectMonitorHeader.add("时间")
                            for (items in result){
                                mselectMonitorItems.add(items as TabelHeaderData)
                                monitorItemsView.append("${items.Description}/")
                                mselectMonitorHeader.add("${items.Description}(${items.Unit})")
                                mselectMonitorComponentID.add(items.ComponentID)
                            }
                            //监测框数据填充
                            monitorItems.value.text = monitorItemsView.toString().trim('/')
                            //监测表头数据填充
                            val mselectCounts = mselectMonitorHeader.size
                            val headerManager = GridLayoutManager(this@DayDataActivity,mselectCounts)
                            recyclerHeader.layoutManager = headerManager
                            mHeaderAdapter = HeaderAdapter(this@DayDataActivity)
                            recyclerHeader.adapter = mHeaderAdapter
                            mHeaderAdapter?.setData(mselectMonitorHeader)

                            refreshLayout.autoRefresh()
                            monitorItems.setExpand(false)
                        }
                        override fun onCancel() {
                            monitorItems.setExpand(false)
                        }
                    })
                    monitorDialog?.show()
                }
            }
            override fun onShrink() {
                monitorDialog?.dismiss()
            }
        })

        //点击查询按钮
        getDatas.setOnClickListener {
            if(mselectMonitorItems.isEmpty()){
                Toast.makeText(mContext
                        , this.resources.getString(R.string.please_select_monitorItems)
                        , Toast.LENGTH_SHORT).show()
            }else{
                refreshLayout.autoRefresh()
            }
        }
        initRecyclerView()
        initRefreshLayout()
    }

    /**
     * 日期初始化
     */
    @SuppressLint("SimpleDateFormat")
    private fun initDateTime() {
        val endDateTime = Date()
        val startDateTime = Date(System.currentTimeMillis()-24*60*60*1000)
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        startDate = sdf.format(startDateTime)
        endDate = sdf.format(endDateTime)
    }

    /**
     * recyclerView初始化
     * */
    private fun initRecyclerView() {
        Log.i(TAG, "init: initRecyclerView")
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerhisview.layoutManager = linearLayoutManager
        mAdapter = HistoricalDataAdapter(this)
        mAdapter?.setOnItemClickListener(object : HistoricalDataAdapter.OnItemClickListener {
            override fun setOnclickItem(position: Int) {
                Log.i(TAG, "setOnclickItem: pos = $position")
            }
        })
        recyclerhisview.adapter = mAdapter
    }

    /**
     * loading初始化
     * */
    private fun initRefreshLayout() {
        Log.i(TAG, "init: initRefreshLayout")
        refreshLayout.setEnableLoadMore(true)
        refreshLayout.setOnRefreshListener {
            mCurrentPageNumber = 1
            getHistoricalData(false)
        }
        refreshLayout.setOnLoadMoreListener {
            getHistoricalData(true)
        }
        loading.setRetryListener {
            loading.showContent()
            refreshLayout.autoRefresh()
        }
        //触发自动刷新
        loading.showContent()
        refreshLayout.autoRefresh()
    }

    /**
     * 查询数据
     */
    private fun getHistoricalData(isLoadMore: Boolean){
        Log.i(TAG, "initData: getHistoricalData")
        val conditionParams = HashMap<String, Any>()
        if (mSelectStation != null){
            conditionParams[AirConstants.ID] = mSelectStation?.key.toString()
        }

        val timeParams = HashMap<String,Any>()
        timeParams[AirConstants.STARTIME] = start_date.value.text.toString()
        timeParams[AirConstants.ENDTIME] = end_date.value.text.toString()
        Log.i(TAG, "timeParams: ${timeParams[AirConstants.STARTIME]}")
        val orderParams = HashMap<String, Any>()
        orderParams[AirConstants.COMPONENTTIME] = "2"
        val params = HashMap<String, String>()
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        params[AirConstants.TIME] = AirUtils.mapTojson(timeParams)
        params[AirConstants.ORDER] = AirUtils.mapTojson(orderParams)
        params[AirConstants.TYPE] = "HourData"
        params[AirConstants.PAGENUMBER] = mCurrentPageNumber.toString()
        params[AirConstants.PAGESIZE] = PAGE_COUNT.toString()
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_HISTORICAL_DATA)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<HistoricalDatas>>>() {
                    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<HistoricalDatas>>?) {
                        Log.i(TAG, "onSuccess: 信息获取成功")
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            if (d != null){
                                showData(d.data, isLoadMore)
                            }else{
                                showEmpty(isLoadMore)
                            }
                        }else if (statusCode == AirConstants.RESPONSE_STATUS_FAILURE){
                            showEmpty(isLoadMore)
                        }
                    }

                    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 信息获取失败")
                        showRetry(isLoadMore)
                    }
                })
    }

    /**
     * 展示数据
     * */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showData(data: ArrayList<HistoricalDatas>, isLoadMore: Boolean) {
        if (isDestroyed) {
            return
        }
        if (isLoadMore) {
            refreshLayout.finishLoadMore()
            mCurrentPageNumber++
            mDataList?.addAll(data)
        } else {
            refreshLayout.finishRefresh()
            mCurrentPageNumber = 1
            mDataList?.clear()
            mDataList = data
        }
        mAdapter?.setData(mDataList!!,mselectMonitorItems)
        mAdapter?.notifyDataSetChanged()
        loading.showContent()
    }

    /**
     * 没有数据 或者没有更多
     * */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showEmpty(isLoadMore: Boolean){
        Log.i(TAG, "showEmpty: ")
        if (isDestroyed){
            return
        }
        if (isLoadMore){
            refreshLayout.finishLoadMoreWithNoMoreData()
        }else{
            refreshLayout.finishRefresh()
            loading.showEmpty()
        }
    }

    /**
     * 展示重新刷新界面
     * */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showRetry(isLoadMore: Boolean) {
        Log.i(TAG, "showRetry: ")
        if (isDestroyed) {
            return
        }
        if (isLoadMore) {
            refreshLayout.finishLoadMore()
        } else {
            refreshLayout.finishRefresh()
            loading.setErrorText("数据获取失败")
            loading.showError()
        }
    }
    override fun initIntentData() {}

    override fun initData() {}

    /**
     * 获取站点信息，并根据站点信息获取表头信息
     */
    private fun initPoint(){
        //取站点信息
        Log.i(TAG, "initData: Point")
        val params = HashMap<String, String>()
        params[AirConstants.TYPE] = ""
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_AIR_STATION_TREE)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<TestPoint>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<TestPoint>>?) {
                        Toast.makeText(mContext
                                , mContext?.resources?.getString(R.string.get_stations_success)
                                , Toast.LENGTH_SHORT).show()
                        handlePoint(d)
                        initTableHeaderInfo()
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 站点信息获取失败")
                        Toast.makeText(mContext
                                , mContext?.resources?.getString(R.string.get_stations_fail)
                                , Toast.LENGTH_SHORT).show()
                    }
                })
    }

    /**
     * 获取表头信息
     */
    private fun initTableHeaderInfo(){
        Log.i(TAG, "initData: initTableHeaderInfo")
        val conditionParams = HashMap<String, Any>()
        conditionParams[AirConstants.ID] = mSelectStation?.key.toString()
        Log.i(TAG, "conditionParams[AirConstants.ID]: ${conditionParams[AirConstants.ID]}")
        conditionParams[AirConstants.POLLUTIONTYPE] = "air"
        val params = HashMap<String, String>()
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        params[AirConstants.TIMELINESS] = "historical"
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_TABLE_HEADER_DATA)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<TabelHeaderData>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<TabelHeaderData>>?) {
                        Toast.makeText(mContext
                                , mContext?.resources?.getString(R.string.get_monitorItems_success)
                                , Toast.LENGTH_SHORT).show()
                        handleTabelHeader(d)
                    }
                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 站点信息获取失败")
                        Toast.makeText(mContext
                                , mContext?.resources?.getString(R.string.get_monitorItems_fail)
                                , Toast.LENGTH_SHORT).show()
                    }
                })
    }

    /**
     * 处理监测点数据
     * */
    @SuppressLint("SetTextI18n")
    private fun handlePoint(d: WanResponse<ArrayList<TestPoint>>?) {
        d?.let {
            mProvinces = d.data
            //默认选择的站点
            mSelectProvince = d.data[0]
            if (mSelectProvince != null){
                mSelectCity = mSelectProvince!!.children?.get(0)
            }
            if (mSelectCity != null){
                mSelectCounty = mSelectCity!!.children?.get(0)
            }
            if (mSelectCounty != null){
                mSelectStation = mSelectCounty!!.children?.get(0)
                historyPoint.value.text =
                        "${mSelectCounty?.title}/${mSelectStation?.title}"
            }else{
                historyPoint.value.text =
                        "暂无站点可选"
            }
        }
    }

    /**
     * 处理表头信息数据
     * */
    private fun handleTabelHeader(d: WanResponse<ArrayList<TabelHeaderData>>?) {
        d?.let {
            mMonitorItems = d.data
        }
        loading.showContent()
    }
    override fun initToolbar() {
        toolbar.title.text = resources.getString(R.string.day_data)
        toolbar.back.setImageResource(R.mipmap.toolbar_back)
        toolbar.back.setOnClickListener {
            finish()
        }
    }

}


