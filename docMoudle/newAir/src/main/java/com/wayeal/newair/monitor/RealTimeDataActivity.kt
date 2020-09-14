package com.wayeal.newair.monitor

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.monitor.adapter.RealTimeDataAdapter
import com.wayeal.newair.monitor.bean.*
import kotlinx.android.synthetic.main.activity_real_time_data.*
import kotlinx.android.synthetic.main.activity_real_time_data.toolbar
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.common_item_with_arrow.view.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RealTimeDataActivity : BaseRealTimeUIActivity() {

    private val TAG = "RealTimeDataActivity"
    private var mRealTimeListData: RealTimeListData? = null

    override fun returnPageType(): RealTimePageType {
        return RealTimePageType.DATA
    }

    companion object {
        const val DATA = "data"
    }

    private var mAdapter: RealTimeDataAdapter? = null
    private var mDataList: ArrayList<RealTimeData>? = ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.activity_real_time_data
    }

    override fun initView() {
        super.initView()
        getRealTimeData.setOnClickListener {
            realTimeDataLoading.showContent()
            realTimeDataRefreshLayout.autoRefresh()
            reStartTime()
        }
        initRecyclerView()
        initRefreshLayout()
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        realTimeDataRecyclerview.layoutManager = linearLayoutManager
        mAdapter = RealTimeDataAdapter(this)
        mAdapter?.setOnItemClickListener(object : RealTimeDataAdapter.OnItemClickListener {
            override fun setOnclickItem(position: Int) {

            }
        })
        realTimeDataRecyclerview.adapter = mAdapter
    }

    /**
     * loading初始化
     * */
    private fun initRefreshLayout() {
        realTimeDataRefreshLayout.setEnableLoadMore(true)
        realTimeDataRefreshLayout.setOnRefreshListener {
            getTestPointInfo(false)
        }
        realTimeDataRefreshLayout.setOnLoadMoreListener {
            getTestPointInfo(true)
        }
        realTimeDataLoading.setRetryListener {
            realTimeDataLoading.showContent()
            realTimeDataRefreshLayout.autoRefresh()
        }
        //触发自动刷新
        realTimeDataLoading.showContent()
        realTimeDataRefreshLayout.autoRefresh()
    }

    override fun initIntentData() {
        mRealTimeListData = intent.getSerializableExtra(DATA) as? RealTimeListData
    }

    override fun initToolbar() {
        toolbar.title.text = resources.getString(R.string.real_time_data)
        toolbar.back.visibility = View.VISIBLE
        toolbar.back.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
        val params = HashMap<String, String>()
        params[AirConstants.TYPE] = ""
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_AIR_STATION_TREE)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<TestPoint>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<TestPoint>>?) {
                        Toast.makeText(this@RealTimeDataActivity
                                , this@RealTimeDataActivity.resources.getString(R.string.get_stations_success)
                                , Toast.LENGTH_SHORT).show()
                        handle(d)
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Toast.makeText(this@RealTimeDataActivity
                                , this@RealTimeDataActivity.resources.getString(R.string.get_stations_fail)
                                , Toast.LENGTH_SHORT).show()
                    }
                })
    }

    /**
     * 处理监测点数据
     * */
    private fun handle(d: WanResponse<ArrayList<TestPoint>>?) {
        d?.let {
            mProvinces = d.data
        }
        if (mRealTimeListData != null){
            Log.i(TAG, "handle: 预处理站点")
            for (province in mProvinces){
                if (province.title == mRealTimeListData?.ProvinceName){
                    mSelectProvince = province
                    break
                }
            }
            for (city in mSelectProvince?.children!!){
                if (city.title == mRealTimeListData?.CityName){
                    mSelectCity = city
                    break
                }
            }
            for (county in mSelectCity?.children!!){
                if (county.title == mRealTimeListData?.CountyName){
                    mSelectCounty = county
                    break
                }
            }
            for (station in mSelectCounty?.children!!){
                if (station.title == mRealTimeListData?.Name){
                    mSelectStation = station
                    break
                }
            }

        }else{
            mSelectProvince = mProvinces[0]
            mSelectCity = mSelectProvince!!.children?.get(0)
            mSelectCounty = mSelectCity!!.children?.get(0)
            mSelectStation = mSelectCounty!!.children?.get(0)
        }
        monitorPoint.value.text = mSelectStation?.title
        getTestPointInfo(false)
    }

    /**
     * 获取实时数据
     * */
    private fun getTestPointInfo(isLoadMore: Boolean) {
        Log.i(TAG, "getTestPointInfo: ")
        val conditionParams = HashMap<String, Any>()
        conditionParams[AirConstants.POLLUTIONTYPE] = "air"
        if (mSelectStation == null) {
            Log.i(TAG, "getTestPointInfo: 没有选择站点 无法查询")
            return
        }
        conditionParams[AirConstants.ID] = mSelectStation?.key.toString()
        val params = HashMap<String, String>()
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_REAL_TIME_DATA)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<RealTimeData>>>() {
                    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<RealTimeData>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            if (d != null) {
                                showData(d.data, isLoadMore)
                            } else {
                                showEmpty(isLoadMore)
                            }
                        } else if (statusCode == AirConstants.RESPONSE_STATUS_FAILURE) {
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
    private fun showData(data: ArrayList<RealTimeData>, isLoadMore: Boolean) {
        Log.i(TAG, "showData: data size = ${data.size}")
        if (isDestroyed) {
            return
        }
        if (isLoadMore) {
            realTimeDataRefreshLayout.finishLoadMore()
            mDataList?.addAll(data)
        } else {
            realTimeDataRefreshLayout.finishRefresh()
            mDataList?.clear()
            mDataList = data
        }
        mAdapter?.setData(mDataList!!)
        mAdapter?.notifyDataSetChanged()
        realTimeDataLoading.showContent()
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
            realTimeDataRefreshLayout.finishLoadMore()
        } else {
            realTimeDataRefreshLayout.finishRefresh()
            realTimeDataLoading.setErrorText("数据获取失败")
            realTimeDataLoading.showError()
        }
    }

    /**
     * 没有数据 或者没有更多
     * */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showEmpty(isLoadMore: Boolean) {
        Log.i(TAG, "showEmpty: ")
        if (isDestroyed) {
            return
        }
        if (isLoadMore) {
            realTimeDataRefreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            realTimeDataRefreshLayout.finishRefresh()
            realTimeDataLoading.showEmpty()
        }
    }
}