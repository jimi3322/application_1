package com.wayeal.newair.monitor

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.wayeal.newair.R
import com.wayeal.newair.base.BaseActivity
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.dialog.ChoosePointDialog
import com.wayeal.newair.common.dialog.CommonListDialog
import com.wayeal.newair.common.view.ChooseView
import com.wayeal.newair.monitor.bean.CityPoint
import com.wayeal.newair.monitor.bean.CountyPoint
import com.wayeal.newair.monitor.bean.TestPoint
import com.wayeal.newair.monitor.bean.TestPointDetail
import kotlinx.android.synthetic.main.activity_real_time_data.*
import kotlinx.android.synthetic.main.activity_real_time_list.*
import kotlinx.android.synthetic.main.activity_real_time_list.refreshTime
import kotlinx.android.synthetic.main.common_item_with_arrow.view.*
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseRealTimeUIActivity : BaseActivity() {

    protected var mFragmentType: RealTimePageType? = null

    enum class RealTimePageType {
        LIST, DATA
    }

    abstract fun returnPageType(): RealTimePageType

    protected var mSessionStatus: String? = null                //会话状态
    protected var mSystemStatus: String? = null                 //系统状态

    protected var mProvinces: ArrayList<TestPoint> = ArrayList()        //获取的站点信息
    protected var mSelectStation: TestPointDetail? = null               //选择的监测站点
    protected var mSelectCounty: CountyPoint? = null                    //选择的区
    protected var mSelectCity: CityPoint? = null                        //选择的城市
    protected var mSelectProvince: TestPoint? = null                    //选择的省

    protected var mSecond = 59        //倒计时
    protected var mHandler: RefreshTime? = RefreshTime(this)


    override fun initView() {
        mFragmentType = returnPageType()

        when (mFragmentType) {
            RealTimePageType.LIST -> {
                initSessionStateStatus()
                initSystemStateStatus()
                initSelectStation()
            }
            RealTimePageType.DATA -> {
                initMonitorStation()
                currentTime.hideArrow()
            }
        }
        refreshTime.hideArrow()
        reStartTime()
    }

    private fun initSessionStateStatus() {
        sessionState.setValue(resources.getString(R.string.all))
        val sessionDialog = CommonListDialog(this
                , getString(R.string.session_state)
                , AirUtils.getStringList(R.array.SessionStates)
                , object : CommonListDialog.onItemClickListener {
            override fun onResult(pos: Int, result: String) {
                sessionState.setValue(result)
                sessionState.setExpand(false)
                mSessionStatus = result
            }
        })
        sessionState.setExpandListener(object : ChooseView.OnChooseClickListener {
            override fun onShrink() {
                sessionDialog.dismiss()
            }

            override fun onExpand() {
                sessionDialog.show()
            }
        })
    }

    private fun initSystemStateStatus() {
        systemState.setValue(resources.getString(R.string.all))
        val systemDialog = CommonListDialog(this
                , getString(R.string.system_state)
                , AirUtils.getStringList(R.array.SystemStates)
                , object : CommonListDialog.onItemClickListener {
            override fun onResult(pos: Int, result: String) {
                systemState.value.text = result
                systemState.setExpand(false)
                mSystemStatus = result
            }
        })
        systemState.setExpandListener(object : ChooseView.OnChooseClickListener {
            override fun onShrink() {
                systemDialog.dismiss()
            }

            override fun onExpand() {
                systemDialog.show()
            }
        })
    }

    private fun initSelectStation() {
        testPoint.setValue(resources.getString(R.string.all))
        var stationDialog: ChoosePointDialog? = null
        testPoint.setExpandListener(object : ChooseView.OnChooseClickListener {
            override fun onExpand() {
                if (mProvinces.size == 0) {
                    Toast.makeText(this@BaseRealTimeUIActivity
                            , resources.getString(R.string.station_info_not_get)
                            , Toast.LENGTH_SHORT).show()
                    testPoint.setExpand(false)
                } else {
                    stationDialog = ChoosePointDialog(this@BaseRealTimeUIActivity
                            , getString(R.string.station)
                            , mProvinces
                            , mSelectProvince
                            , mSelectCity
                            , mSelectCounty
                            , mSelectStation
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
                            if (mSelectStation == null) {
                                testPoint.value.text =
                                        "${mSelectCounty?.title}/全部"
                            } else {
                                testPoint.value.text =
                                        "${mSelectCounty?.title}/${mSelectStation?.title}"
                            }
                            testPoint.setExpand(false)
                            realTimeListLoading.showContent()
                            realTimeListRefreshLayout.autoRefresh()

                            reStartTime()
                        }

                        override fun onCancel() {
                            testPoint.setExpand(false)
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

    private fun initMonitorStation() {
        var stationDialog: ChoosePointDialog? = null
        monitorPoint.setExpandListener(object : ChooseView.OnChooseClickListener {
            override fun onExpand() {
                if (mProvinces.size == 0) {
                    Toast.makeText(this@BaseRealTimeUIActivity
                            , resources.getString(R.string.station_info_not_get)
                            , Toast.LENGTH_SHORT).show()
                    monitorPoint.setExpand(false)
                } else {
                    stationDialog = ChoosePointDialog(this@BaseRealTimeUIActivity
                            , getString(R.string.station)
                            , mProvinces
                            , mSelectProvince
                            , mSelectCity
                            , mSelectCounty
                            , mSelectStation
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
                            if (mSelectStation == null) {
                                monitorPoint.value.text =
                                        "${mSelectCounty?.title}/全部"
                            } else {
                                monitorPoint.value.text =
                                        "${mSelectCounty?.title}/${mSelectStation?.title}"
                            }
                            realTimeDataLoading.showContent()
                            realTimeDataRefreshLayout.autoRefresh()
                            reStartTime()
                            monitorPoint.setExpand(false)
                        }

                        override fun onCancel() {
                            monitorPoint.setExpand(false)
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
     * 刷新定时器
     * */
    protected fun reStartTime() {
        mSecond = 60
        mHandler?.removeMessages(REFRESH_TEXT)
        mHandler?.removeMessages(REFRESH_DATA)
        mHandler?.sendEmptyMessage(REFRESH_TEXT)
    }

    /**
     * 倒计时刷新时间
     * */
    @SuppressLint("HandlerLeak")
    inner class RefreshTime(activity: Activity) : Handler() {
        private var mActivity: WeakReference<Activity>? = null

        init {
            mActivity = WeakReference(activity)
        }

        @SuppressLint("SetTextI18n")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                REFRESH_TEXT -> {
                    refreshTime.setValue("${mSecond}s")
                    mSecond--
                    mHandler?.sendEmptyMessageDelayed(REFRESH_TEXT, 1000)
                    if (mSecond == 0) {
                        mHandler?.sendEmptyMessage(REFRESH_DATA)
                    }
                    when(mFragmentType){
                        RealTimePageType.DATA -> {
                            currentTime.setValue(AirUtils.getTimeWithFormat(Date(),"yyyy/MM/dd HH:mm:ss"))
                        }
                    }
                }
                REFRESH_DATA -> {
                    reStartTime()
                    when(mFragmentType){
                        RealTimePageType.LIST -> {
                            realTimeListLoading.showContent()
                            realTimeListRefreshLayout.autoRefresh()
                        }
                        RealTimePageType.DATA -> {
                            realTimeDataLoading.showContent()
                            realTimeDataRefreshLayout.autoRefresh()
                        }
                    }

                }
            }
        }
    }
}