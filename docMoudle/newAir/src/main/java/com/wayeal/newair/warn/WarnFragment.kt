package com.wayeal.newair.warn

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.SpaceItemDecoration
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.common.dialog.ChoosePointDialog
import com.wayeal.newair.common.dialog.CommonListDialog
import com.wayeal.newair.common.view.ChooseView
import com.wayeal.newair.monitor.bean.*
import com.wayeal.newair.warn.adapter.WarnAdapter
import com.wayeal.newair.warn.bean.WarnData
import kotlinx.android.synthetic.main.activity_real_time_list.*
import kotlinx.android.synthetic.main.common_item_with_arrow.view.*
import kotlinx.android.synthetic.main.fragment_warn.*
import kotlinx.android.synthetic.main.fragment_warn.loading
import kotlinx.android.synthetic.main.fragment_warn.recyclerview
import kotlinx.android.synthetic.main.fragment_warn.refreshLayout
import java.util.*
import kotlin.collections.HashMap

const val TAG = "WarnFragment"

class WarnFragment : Fragment() {

    private var mSelectStation: TestPointDetail? = null       //选择的监测站点
    private var mSelectCounty: CountyPoint? = null      //选择的区
    private var mSelectCity: CityPoint? = null          //选择的城市
    private var mSelectProvince: TestPoint? = null      //选择的省

    private var mProvinces: ArrayList<TestPoint> = ArrayList()        //获取的站点信息
    private var mCurrentPageNumber = 1
    private var mAdapter: WarnAdapter? = null
    private var mDataList: ArrayList<WarnData>? = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_warn, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    companion object {
        @JvmStatic
        fun newInstance() = WarnFragment()
    }

    private fun initView(){
        initItemView()
        initRecyclerView()
        initRefreshLayout()
    }

    /**
     * 先获取站点树 再进行加载站点报警信息
     * */
    private fun initData(){
        //取站点信息
        Log.i(TAG, "initData: ")
        val params = HashMap<String, String>()
        params[AirConstants.TYPE] = ""
        OkHttpUtils.getInstance()
                .post(activity, HttpUtil.getAirServiceAddress(AirConstants.GET_AIR_STATION_TREE)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<TestPoint>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<TestPoint>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            Log.i(TAG, "onSuccess: 站点信息获取成功")
                            if (d?.data != null){
                                mProvinces = d.data
                                mSelectProvince = mProvinces[0]
                                mSelectCity = mSelectProvince?.children?.get(0)
                                mSelectCounty = mSelectCity?.children?.get(0)
                                mSelectStation = mSelectCounty?.children?.get(0)
                                station.setValue(mSelectStation?.title)
                                getWarnData(false)
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 站点信息获取失败")
                    }
                })
    }

    /**
     * 查询报警信息
     * */
    private fun getWarnData(isLoadMore: Boolean){
        Log.i(TAG, "getWarnData: ")
        val areaParams = HashMap<String, Any>()
        areaParams[AirConstants.ID] = mSelectStation?.key.toString()
        areaParams[AirConstants.POLLUTIONTYPE] = "air"
        val timeParams = HashMap<String, Any>()
        timeParams[AirConstants.STARTIME] = startTime.value.text.toString()
        timeParams[AirConstants.ENDTIME] = endTime.value.text.toString()
        //排序
        val orderParams = HashMap<String,Any>()
        orderParams[AirConstants.STARTDATE] = 2.toString()
        orderParams[AirConstants.NAME] = 5.toString()
        val params = HashMap<String, String>()
        params[AirConstants.AREA] = AirUtils.mapTojson(areaParams)
        params[AirConstants.TIME] = AirUtils.mapTojson(timeParams)
        params[AirConstants.ORDER] = AirUtils.mapTojson(orderParams)
        params[AirConstants.PAGENUMBER] = mCurrentPageNumber.toString()
        params[AirConstants.PAGESIZE] = 10.toString()
        OkHttpUtils.getInstance()
                .post(activity, HttpUtil.getAirServiceAddress(AirConstants.GET_AREA_ALARM_DATA)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<WarnData>>>() {
                    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<WarnData>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            Log.i(TAG, "onSuccess: 报警信息获取成功")
                            if (d != null){
                                showData(d.data,isLoadMore)
                            }else{
                                showEmpty(isLoadMore)
                            }
                        }else if (statusCode == AirConstants.RESPONSE_STATUS_FAILURE){
                            showEmpty(isLoadMore)
                        }
                    }

                    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 站点信息获取失败")
                        showRetry(isLoadMore)
                    }
                })
    }

    protected lateinit var stDate: Date       //开始时间
    protected lateinit var edDate: Date       //结束时间

    /**
     * 初始化itemView
     * */
    private fun initItemView(){
        //开始时间
        stDate = Date(System.currentTimeMillis() - 24 * 3600 * 1000)
        startTime.setValue(AirUtils.getTime(stDate))
        val startTimeDialog = TimePickerBuilder(activity, OnTimeSelectListener {
            date, _ ->
            Log.i(TAG, "onTimeSelect: ${AirUtils.getTime(date!!)}")
            startTime.setExpand(false)
            stDate = date
            startTime.setValue(AirUtils.getTime(date))
        }).setType(booleanArrayOf(true, true, true, true, true, false))
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
        startTime.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onShrink() {
                startTimeDialog.dismiss()
            }

            override fun onExpand() {
                startTimeDialog.show()
            }
        })

        //结束时间
        edDate = Date(System.currentTimeMillis())
        endTime.setValue(AirUtils.getTime(edDate))
        val endTimeDialog = TimePickerBuilder(activity, OnTimeSelectListener {
            date, _ ->
            Log.i(TAG, "onTimeSelect: ${AirUtils.getTime(date!!)}")
            endTime.setExpand(false)
            stDate = date
            endTime.setValue(AirUtils.getTime(date))
        }).setType(booleanArrayOf(true, true, true, true, true, false))
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
        endTime.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onShrink() {
                endTimeDialog.dismiss()
            }

            override fun onExpand() {
                endTimeDialog.show()
            }
        })
        //报警类型
        alarmType.setValue(resources.getString(R.string.all))
        val alarmDialog = CommonListDialog(activity!!
                , getString(R.string.alarm_type)
                , AirUtils.getStringList(R.array.WarningTypes)
                , object : CommonListDialog.onItemClickListener {
            override fun onResult(pos: Int, result: String) {
                alarmType.setValue(result)
                alarmType.setExpand(false)
            }
        })
        alarmType.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onShrink() {
                alarmDialog.dismiss()
            }

            override fun onExpand() {
                Log.i(TAG, "onExpand: ")
                alarmDialog.show()
            }
        })

        //状态
        state.setValue(resources.getString(R.string.all))
        val stateDialog = CommonListDialog(activity!!
                , getString(R.string.state)
                , AirUtils.getStringList(R.array.StateItems)
                , object : CommonListDialog.onItemClickListener {
            override fun onResult(pos: Int, result: String) {
                alarmType.setValue(result)
                alarmType.setExpand(false)
            }
        })
        state.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onShrink() {
                stateDialog.dismiss()
            }

            override fun onExpand() {
                Log.i(TAG, "onExpand: ")
                stateDialog.show()
            }
        })

        //监测站点
        var stationDialog: ChoosePointDialog? = null
        station.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                if (mProvinces.size == 0){
                    Toast.makeText(activity, "站点信息还没有获取到 稍后再试", Toast.LENGTH_SHORT).show()
                    station.setExpand(false)
                }else{
                    stationDialog = ChoosePointDialog(activity!!
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
        //查询
        getWarnData.setOnClickListener {
            getWarnData(false)
        }
    }

    /**
     * recyclerView初始化
     * */
    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerview.layoutManager = linearLayoutManager
        mAdapter = WarnAdapter(activity!!)
        mAdapter?.setOnItemClickListener(object : WarnAdapter.OnWarnItemClickListener{
            override fun onClickDetail(position: Int) {
                Log.i(TAG, "onClickDetail: ")
                val warnDetailDialog = mDataList?.get(position)?.let { WarningDetailDialog(activity!!, it) }
                warnDetailDialog?.show()
            }

            override fun onClickHandle(position: Int) {
                Log.i(TAG, "onClickHandle: ")
                mDataList?.get(position)?.let {
                    val warningHandleDialog = WarningHandleDialog(activity!!,it,object : WarningHandleDialog.onWarningHandleListener{
                        override fun onCancel() {

                        }

                        override fun onConfirm() {
                            loading.showContent()
                            refreshLayout.autoRefresh()
                        }
                    })
                    warningHandleDialog.show()
                }
            }
        })
        recyclerview.addItemDecoration(SpaceItemDecoration(bottom = 5))
        recyclerview.adapter = mAdapter
    }

    /**
     * loading初始化
     * */
    private fun initRefreshLayout() {
        refreshLayout.setEnableLoadMore(true)
        refreshLayout.setOnRefreshListener {
            mCurrentPageNumber = 1
            getWarnData(false)
        }
        refreshLayout.setOnLoadMoreListener {
            getWarnData(true)
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
     * 展示数据
     * */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showData(data: ArrayList<WarnData>, isLoadMore: Boolean) {
        Log.i(TAG, "showData: data size = ${data.size}")
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
        mAdapter?.setData(mDataList!!)
        mAdapter?.notifyDataSetChanged()
        loading.showContent()
    }

    /**
     * 展示重新刷新界面
     * */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showRetry(isLoadMore: Boolean) {
        Log.i(TAG, "showRetry: ")
        if (isLoadMore) {
            refreshLayout.finishLoadMore()
        } else {
            refreshLayout.finishRefresh()
            loading.setErrorText("数据获取失败")
            loading.showError()
        }
    }

    /**
     * 没有数据 或者没有更多
     * */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showEmpty(isLoadMore: Boolean){
        Log.i(TAG, "showEmpty: ")
        if (isLoadMore){
            refreshLayout.finishLoadMoreWithNoMoreData()
        }else{
            refreshLayout.finishRefresh()
            loading.showEmpty()
        }
    }
}