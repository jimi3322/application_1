package com.wayeal.newair.monitor

import android.content.Intent
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
import com.wayeal.newair.common.SpaceItemDecoration
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.monitor.adapter.RealTimeListAdapter
import com.wayeal.newair.monitor.bean.*
import kotlinx.android.synthetic.main.activity_real_time_list.*
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.fragment_statistic.toolbar

const val PAGE_COUNT = 10
const val REFRESH_TEXT = 1001       //刷新时间
const val REFRESH_DATA = 1002       //刷新任务

class RealTimeListActivity : BaseRealTimeUIActivity() {

    private val TAG = "RealTimeListActivity"
    private var mCurrentPageNumber = 1
    private var mAdapter: RealTimeListAdapter? = null
    private var mDataList: ArrayList<RealTimeListData>? = ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.activity_real_time_list
    }

    override fun returnPageType(): RealTimePageType {
        return RealTimePageType.LIST
    }

    override fun initView() {
        super.initView()
        getRealTimeListData.setOnClickListener {
            realTimeListLoading.showContent()
            realTimeListRefreshLayout.autoRefresh()
            reStartTime()
        }
        initRecyclerView()
        initRefreshLayout()
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        realTimeListRecyclerView.layoutManager = linearLayoutManager
        mAdapter = RealTimeListAdapter(this)
        mAdapter?.setOnItemClickListener(object : RealTimeListAdapter.OnItemClickListener {
            override fun setOnclickItem(position: Int) {
                val intent = Intent(this@RealTimeListActivity,RealTimeDataActivity::class.java)
                intent.putExtra(RealTimeDataActivity.DATA, mDataList?.get(position))
                startActivity(intent)
            }
        })
        realTimeListRecyclerView.addItemDecoration(SpaceItemDecoration(bottom = 5))
        realTimeListRecyclerView.adapter = mAdapter

    }

    private fun initRefreshLayout() {
        realTimeListRefreshLayout.setEnableLoadMore(true)
        realTimeListRefreshLayout.setOnRefreshListener {
            mCurrentPageNumber = 1
            getTestPointInfo(false)
        }
        realTimeListRefreshLayout.setOnLoadMoreListener {
            getTestPointInfo(true)
        }
        realTimeListLoading.setRetryListener {
            realTimeListLoading.showContent()
            realTimeListRefreshLayout.autoRefresh()
        }
        realTimeListLoading.showContent()
        realTimeListRefreshLayout.autoRefresh()
    }

    override fun initIntentData() {

    }

    override fun initData() {
        val params = HashMap<String, String>()
        params[AirConstants.TYPE] = ""
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_AIR_STATION_TREE)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<TestPoint>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<TestPoint>>?) {
                        Toast.makeText(this@RealTimeListActivity
                                , this@RealTimeListActivity.resources.getString(R.string.get_stations_success)
                                , Toast.LENGTH_SHORT).show()
                        handAirStationTree(d)
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Toast.makeText(this@RealTimeListActivity
                                , this@RealTimeListActivity.resources.getString(R.string.get_stations_fail)
                                , Toast.LENGTH_SHORT).show()
                    }
                })
    }

    /**
     * 获取监测点信息
     * */
    private fun getTestPointInfo(isLoadMore: Boolean) {
        val conditionParams = HashMap<String, Any>()
        conditionParams[AirConstants.POLLUTIONTYPE] = "air"
        mSessionStatus?.let {
            conditionParams[AirConstants.STATUS] = when (mSessionStatus) {
                AirConstants.ONLINE_TEXT -> AirConstants.ONLINE
                AirConstants.UPLINE_TEXT -> AirConstants.UPLINE
                AirConstants.OFFLINE_TEXT -> AirConstants.OFFLINE
                else -> ""
            }
        }
        mSystemStatus?.let {
            conditionParams[AirConstants.SYSTEM_STATUS] = when (mSystemStatus) {
                AirConstants.NORMAL_TEXT -> AirConstants.NORMAL
                AirConstants.SUPER_UPPER_LIMIT_TEXT -> AirConstants.SUPER_UPPER_LIMIT
                AirConstants.SUPER_LOWER_LIMIT_TEXT -> AirConstants.SUPER_LOWER_LIMIT
                AirConstants.INSTRUMENT_FAILURE_TEXT -> AirConstants.INSTRUMENT_FAILURE
                AirConstants.INSTRUMENT_COMMUNICATION_FAILURE_TEXT -> AirConstants.INSTRUMENT_COMMUNICATION_FAILURE
                AirConstants.INSTRUMENT_OFFLINE_TEXT -> AirConstants.INSTRUMENT_OFFLINE
                AirConstants.MAINTAIN_DEBUG_DATA_TEXT -> AirConstants.MAINTAIN_DEBUG_DATA
                AirConstants.LACK_OF_REAGENTS_TEXT -> AirConstants.LACK_OF_REAGENTS
                AirConstants.LACK_OF_WATER_TEXT -> AirConstants.LACK_OF_WATER
                AirConstants.WATER_SHORTAGE_TEXT -> AirConstants.WATER_SHORTAGE
                AirConstants.LACK_OF_STANDARD_SAMPLE_TEXT -> AirConstants.LACK_OF_STANDARD_SAMPLE
                AirConstants.STANDARD_RECOVERY_TEXT -> AirConstants.STANDARD_RECOVERY
                AirConstants.PARALLEL_SAMPLE_TEST_TEXT -> AirConstants.PARALLEL_SAMPLE_TEST
                else -> ""
            }
        }
        if (mSelectStation == null && mSelectCounty != null) {
            conditionParams[AirConstants.COUNTY_ID] = mSelectCounty?.key.toString()
        } else if (mSelectStation != null && mSelectCounty != null) {
            conditionParams[AirConstants.ID] = mSelectStation?.key.toString()
        }
        val params = HashMap<String, String>()
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        params[AirConstants.PAGENUMBER] = mCurrentPageNumber.toString()
        params[AirConstants.PAGESIZE] = PAGE_COUNT.toString()
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_TESTPOINT_INFO_WITH_USERRIGHTS)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<RealTimeListData>>>() {
                    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<RealTimeListData>>?) {
                        Log.i(TAG, "onSuccess: 信息获取成功")
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

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showData(data: ArrayList<RealTimeListData>, isLoadMore: Boolean) {
        if (isDestroyed) {
            return
        }
        if (isLoadMore) {
            realTimeListRefreshLayout.finishLoadMore()
            mCurrentPageNumber++
            mDataList?.addAll(data)
        } else {
            realTimeListRefreshLayout.finishRefresh()
            mCurrentPageNumber = 1
            mDataList?.clear()
            mDataList = data
        }
        mAdapter?.setData(mDataList!!)
        mAdapter?.notifyDataSetChanged()
        realTimeListLoading.showContent()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showRetry(isLoadMore: Boolean) {
        if (isDestroyed) {
            return
        }
        if (isLoadMore) {
            realTimeListRefreshLayout.finishLoadMore()
        } else {
            realTimeListRefreshLayout.finishRefresh()
            realTimeListLoading.setErrorText("数据获取失败")
            realTimeListLoading.showError()
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showEmpty(isLoadMore: Boolean) {
        Log.i(TAG, "showEmpty: ")
        if (isDestroyed) {
            return
        }
        if (isLoadMore) {
            realTimeListRefreshLayout.finishLoadMoreWithNoMoreData()
        } else {
            realTimeListRefreshLayout.finishRefresh()
            realTimeListLoading.showEmpty()
        }
    }

    private fun handAirStationTree(d: WanResponse<ArrayList<TestPoint>>?) {
        d?.let {
            mProvinces = d.data
        }
    }

    override fun initToolbar() {
        toolbar.title.text = resources.getString(R.string.real_time_view)
        toolbar.back.visibility = View.VISIBLE
        toolbar.back.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler?.removeMessages(REFRESH_TEXT)
        mHandler?.removeMessages(REFRESH_DATA)
        mHandler = null
    }

}