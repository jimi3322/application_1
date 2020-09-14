package com.wayeal.newair.statistic.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.wayeal.newair.R
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.BaseChooseItemType
import com.wayeal.newair.common.dialog.ChoosePointDialog
import com.wayeal.newair.common.dialog.CommonListDialog
import com.wayeal.newair.common.view.ChooseView
import com.wayeal.newair.data.bean.TabelHeaderData
import com.wayeal.newair.monitor.bean.*
import com.wayeal.newair.statistic.MulitiChooseDialog
import com.wayeal.newair.statistic.TAG
import kotlinx.android.synthetic.main.common_item_with_arrow.view.*
import kotlinx.android.synthetic.main.fragment_single_analysis.*
import kotlinx.android.synthetic.main.fragment_single_analysis.station
import java.util.*
import kotlin.collections.ArrayList


/**
 * 本fragment主要做一些公共操作的事务，比如时间选择和站点选择等
 * */
abstract class BaseOperationFragment : BaseBusinessFragment() {

    protected lateinit var stDate: Date       //开始时间日期
    protected lateinit var edDate: Date       //结束时间日期
    protected lateinit var stHour: String     //开始时间小时
    protected lateinit var edHour: String     //结束时间小时

    protected var mSingleSelectFactors: ArrayList<TabelHeaderData> = ArrayList()        //单站点趋势分析选择的监测项目
    protected var mMutliSelectFactors: ArrayList<ComponentBean> = ArrayList()        //多站点趋势分析选择的监测项目
    protected var mMutliSelectStations: ArrayList<TestPointDetail> = ArrayList()        //多站点趋势分析选择的站点

    override fun initView() {
        super.initView()
        initTime()
        when(mFragmentType){
            SINGLE -> {
                initStation()
                initSingleFactor()
            }
            MUTLI -> {
                initMutliStation()
                initMutliFactor()
            }
        }
    }

    private fun initTime() {
        //设置开始时间日期
        when(mFragmentType){
            SINGLE -> {
                stDate = Date(System.currentTimeMillis() - 24 * 3600 * 1000)
            }
            MUTLI -> {
                stDate = Date(System.currentTimeMillis() - 7 * 24 * 3600 * 1000)
            }
        }
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
        //设置开始时间小时
        startHour.setValue(AirUtils.getHour(stDate))
        stHour = AirUtils.getHour(stDate)
        val stHourDialog = CommonListDialog(mActivity
                , getString(R.string.time)
                , AirUtils.getStringList(R.array.TimeHours)
                , object : CommonListDialog.onItemClickListener {
            override fun onResult(pos: Int, result: String) {
                startHour.setValue(result)
                startHour.setExpand(false)
                stHour = result
            }
        })
        startHour.setExpandListener(object : ChooseView.OnChooseClickListener {
            override fun onShrink() {
                stHourDialog.dismiss()
            }

            override fun onExpand() {
                Log.i(TAG, "onExpand: ")
                stHourDialog.show()
            }
        })

        //设置结束时间小时
        endHour.setValue(AirUtils.getHour(edDate))
        edHour = AirUtils.getHour(edDate)
        val edHourDialog = CommonListDialog(mActivity
                , getString(R.string.time)
                , AirUtils.getStringList(R.array.TimeHours)
                , object : CommonListDialog.onItemClickListener {
            override fun onResult(pos: Int, result: String) {
                endHour.setValue(result)
                endHour.setExpand(false)
                edHour = result
            }
        })
        endHour.setExpandListener(object : ChooseView.OnChooseClickListener {
            override fun onShrink() {
                edHourDialog.dismiss()
            }

            override fun onExpand() {
                Log.i(TAG, "onExpand: ")
                edHourDialog.show()
            }
        })
    }

    private fun initStation(){
        var stationDialog: ChoosePointDialog? = null
        station.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                if (mAirStations.size == 0){
                    Toast.makeText(mActivity, "站点信息还没有获取到 稍后再试", Toast.LENGTH_SHORT).show()
                    station.setExpand(false)
                }else{
                    stationDialog = ChoosePointDialog(mActivity
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

    private fun initSingleFactor(){

        var singleFactorDialog: MulitiChooseDialog? = null
        singleFactor.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                if (mFactors.isEmpty()){
                    Toast.makeText(mActivity, "监测站点信息暂未获取成功", Toast.LENGTH_SHORT).show()
                }else{
                    singleFactorDialog = MulitiChooseDialog(context = mActivity
                            , title = "监测项目"
                            , list = mFactors
                            , limitCount = 2
                            , listener = object : MulitiChooseDialog.onItemClickListener{
                        override fun onResult(result: ArrayList<BaseChooseItemType>) {
                            mSingleSelectFactors.clear()
                            for (item in result){
                                Log.i(TAG, "onResult: item = ${(item as TabelHeaderData).Description}")
                                mSingleSelectFactors.add(item)

                            }
                            val showSingleFactor = StringBuffer()
                            for (singlefactor in mSingleSelectFactors){
                                showSingleFactor.append(singlefactor.Description).append(" ")
                            }
                            singleFactor.value.text = showSingleFactor
                            singleFactor.setExpand(false)
                        }
                    })
                    singleFactorDialog!!.show()
                }
            }

            override fun onShrink() {
                singleFactorDialog?.dismiss()
            }
        })
    }

    private fun initMutliFactor(){
        var mutliFactorDialog: MulitiChooseDialog? = null
        singleFactor.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                if (mComponentList.isEmpty()){
                    Toast.makeText(mActivity, "监测站点信息暂未获取成功", Toast.LENGTH_SHORT).show()
                }else{
                    mutliFactorDialog = MulitiChooseDialog(context = mActivity
                            , title = "监测项目"
                            , list = mComponentList
                            , limitCount = 1
                            , listener = object : MulitiChooseDialog.onItemClickListener{
                        override fun onResult(result: ArrayList<BaseChooseItemType>) {
                            mMutliSelectFactors.clear()
                            for (item in result){
                                Log.i(TAG, "onResult: item = ${(item as ComponentBean).Description}")
                                mMutliSelectFactors.add(item)
                            }
                            val showSingleFactor = StringBuffer()
                            for (singlefactor in mMutliSelectFactors){
                                showSingleFactor.append(singlefactor.Description).append(" ")
                            }
                            singleFactor.setValue(showSingleFactor.toString())
                            singleFactor.setExpand(false)
                        }
                    })
                    mutliFactorDialog!!.show()
                }
            }

            override fun onShrink() {
                mutliFactorDialog?.dismiss()
            }
        })
    }


    private fun initMutliStation(){
        var mutliStationDialog: MulitiChooseDialog? = null
        station.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                if (mAllStations.isEmpty()){
                    Toast.makeText(mActivity, "监测站点信息暂未获取成功", Toast.LENGTH_SHORT).show()
                }else{
                    mutliStationDialog = MulitiChooseDialog(context = mActivity
                            , title = "监测项目"
                            , list = mAllStations
                            , limitCount = 4
                            , listener = object : MulitiChooseDialog.onItemClickListener{
                        override fun onResult(result: ArrayList<BaseChooseItemType>) {
                            mMutliSelectStations.clear()
                            for (item in result){
                                Log.i(TAG, "onResult: item = ${(item as TestPointDetail).title}")
                                mMutliSelectStations.add(item)
                            }
                            val showStation = StringBuffer()
                            for (station in mMutliSelectStations){
                                showStation.append(station.title).append(" ")
                            }
                            station.setValue(showStation.toString())
                            station.setExpand(false)
                        }
                    })
                    mutliStationDialog!!.show()
                }
            }

            override fun onShrink() {
                mutliStationDialog?.dismiss()
            }
        })
    }

}