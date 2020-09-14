package com.wayeal.newair.common.dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import com.wayeal.newair.R
import com.wayeal.newair.monitor.bean.CityPoint
import com.wayeal.newair.monitor.bean.CountyPoint
import com.wayeal.newair.monitor.bean.TestPoint
import com.wayeal.newair.monitor.bean.TestPointDetail
import kotlinx.android.synthetic.main.common_item_with_arrow.view.*
import kotlinx.android.synthetic.main.dialog_choose_point.view.*
import kotlinx.android.synthetic.main.dialog_choose_point.view.llSelectCounty
import kotlinx.android.synthetic.main.dialog_list.view.title

class ChoosePointDialog(context: Context
                        , title: String
                        , points: ArrayList<TestPoint>
                        , province: TestPoint?
                        , city: CityPoint?
                        , county: CountyPoint?
                        , station: TestPointDetail?
                        , listener: ConfirmPointListener
                        , isCityType: Boolean = false) :
        Dialog(context, R.style.dialog_style) {

    private val TAG = "ChoosePointDialog"

    private var provinces: ArrayList<TestPoint> = ArrayList()            //省数据

    private var provinceList: ArrayList<String> = ArrayList()           //展示列表
    private var cityList: ArrayList<String> = ArrayList()
    private var countyList: ArrayList<String> = ArrayList()
    private var stationList: ArrayList<String> = ArrayList()

    private var mSelectedProvince: TestPoint? = null                     //选中的点
    private var mSelectedCity: CityPoint? = null
    private var mSelectedCounty: CountyPoint? = null
    private var mSelectedStation: TestPointDetail? = null

    private var mContext: Context? = null
    private var mListener: ConfirmPointListener? = null
    private var mView: View? = null

    init {
        mContext = context
        mListener = listener
        provinces = points
        mSelectedProvince = province
        mSelectedCity = city
        mSelectedCounty = county
        mSelectedStation = station
        val view = View.inflate(context, R.layout.dialog_choose_point, null)
        mView = view
        view.title.text = title
        if (isCityType){
            view.llSelectCounty.visibility = View.GONE
            view.llSelectStation.visibility = View.GONE
        }
        initData(provinces)
        refreshView(view)
        handleView(view)
        //设置view
        setContentView(view)
        //设置显示属性
        initAttributes(view)
    }

    /**
     * 获取到监测点数据
     * */
    private fun initData(provinces: ArrayList<TestPoint>) {
        if (mSelectedProvince == null){
            mSelectedProvince = provinces[0]
        }
        if (mSelectedCity == null){
            mSelectedCity = mSelectedProvince!!.children?.get(0)
        }
        if (mSelectedCounty == null){
            mSelectedCounty = mSelectedCity!!.children?.get(0)
        }
//        mSelectedStation = mSelectedCounty!!.children?.get(0)
        refreshDataList()
    }

    /**
     * 获取需要展示的列表
     * */
    private fun refreshDataList() {
        provinceList.clear()
        for (province in provinces) {
            provinceList.add(province.title)
        }

        cityList.clear()
        for (city in mSelectedProvince?.children!!) {
            cityList.add(city.title)
        }

        countyList.clear()
        for (county in mSelectedCity?.children!!) {
            countyList.add(county.title)
        }

        stationList.clear()
        stationList.add(mContext?.resources!!.getString(R.string.all))
        for (station in mSelectedCounty?.children!!) {
            stationList.add(station.title)
        }
    }

    /**
     * 每次选完之后进行刷新View
     * */
    private fun refreshView(view: View) {
        view.selectProvince.value.text = mSelectedProvince?.title
        view.selectCity.value.text = mSelectedCity?.title
        view.selectCounty.value.text = mSelectedCounty?.title
        if (mSelectedStation == null){
            view.selectStation.value.text = stationList[0]
        }else{
            view.selectStation.value.text = mSelectedStation?.title
        }
    }

    /**
     * 处理点击事件
     * */
    private fun handleView(view: View) {
        view.selectProvince.setOnClickListener {
            val dialog = CommonListDialog(mContext!!
                    , mContext?.resources!!.getString(R.string.please_select_province)
                    , provinceList
                    , object : CommonListDialog.onItemClickListener {
                override fun onResult(pos: Int, result: String) {
                    Log.i(TAG, "onResult: pos = $pos result = $result")
                    view.selectProvince.value.text = result
                    notifyProvince(pos)
                }
            })
            dialog.show()
        }
        view.selectCity.setOnClickListener {
            CommonListDialog(mContext!!
                    , mContext?.resources!!.getString(R.string.please_select_city)
                    , cityList
                    , object : CommonListDialog.onItemClickListener {
                override fun onResult(pos: Int, result: String) {
                    Log.i(TAG, "onResult: pos = $pos result = $result")
                    view.selectCity.value.text = result
                    notifyCity(pos)
                }
            }).show()
        }
        view.selectCounty.setOnClickListener {
            CommonListDialog(mContext!!
                    , mContext?.resources!!.getString(R.string.please_select_county)
                    , countyList
                    , object : CommonListDialog.onItemClickListener {
                override fun onResult(pos: Int, result: String) {
                    Log.i(TAG, "onResult: pos = $pos result = $result")
                    view.selectCounty.value.text = result
                    notifyCounty(pos)
                }
            }).show()
        }
        view.selectStation.setOnClickListener {
            CommonListDialog(mContext!!
                    , mContext?.resources!!.getString(R.string.please_select_station)
                    , stationList
                    , object : CommonListDialog.onItemClickListener {
                override fun onResult(pos: Int, result: String) {
                    Log.i(TAG, "onResult: pos = $pos result = $result")
                    view.selectStation.value.text = result
                    notifyStation(pos)
                }
            }).show()
        }
        view.cancel.setOnClickListener {
            dismiss()
            mListener?.onCancel()
            Log.i(TAG, "handleView: cancel")
        }
        view.confirm.setOnClickListener {
            Log.i(TAG, "handleView: confirm")
            dismiss()
            mListener?.onConfirm(mSelectedProvince
                    ,mSelectedCity
                    ,mSelectedCounty
                    ,mSelectedStation)
        }
    }

    /**
     * 选择省数据
     * */
    private fun notifyProvince(provicePos: Int) {
        mSelectedProvince = provinces[provicePos]
        mSelectedCity = mSelectedProvince!!.children?.get(0)
        mSelectedCounty = mSelectedCity!!.children?.get(0)
        mSelectedStation = null
        refreshDataList()
        refreshView(mView!!)
    }

    /**
     * 选择市数据
     * */
    private fun notifyCity(pos: Int) {
        mSelectedCity = mSelectedProvince!!.children?.get(pos)
        mSelectedCounty = mSelectedCity!!.children?.get(0)
        mSelectedStation = null
        refreshDataList()
        refreshView(mView!!)
    }

    /**
     * 选择区数据
     * */
    private fun notifyCounty(pos: Int) {
        mSelectedCounty = mSelectedCity!!.children?.get(pos)
        mSelectedStation = null
        refreshDataList()
        refreshView(mView!!)
    }

    /**
     * 选择站点数据
     * */
    private fun notifyStation(pos: Int) {
        Log.i(TAG, "notifyStation: ")
        if (pos == 0) {
            Log.i(TAG, "notifyStation: 选择全部")
            mSelectedStation = null
        } else {
            Log.i(TAG, "notifyStation: 选择具体站点")
            mSelectedStation = mSelectedCounty!!.children?.get(pos - 1)
            refreshDataList()
        }

    }


    /**
     * 全屏显示
     * */
    private fun initAttributes(v: View) {
        val windowManager = window?.windowManager
        val display = windowManager?.defaultDisplay
        val lp = this.window?.attributes
        lp?.height = display?.height as Int
        lp?.width = display.width
        this.window?.attributes = lp
    }


    interface ConfirmPointListener {
        fun onConfirm(selectProvince: TestPoint?
                      , selectCity: CityPoint?
                      , selectCounty: CountyPoint?
                      , selectStation: TestPointDetail?)
        fun onCancel()
    }
}