package com.wayeal.newair.statistic.base

import android.util.Log
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.wayeal.newair.base.BaseFragment
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.data.bean.TabelHeaderData
import com.wayeal.newair.monitor.bean.*
import com.wayeal.newair.statistic.TAG
import kotlinx.android.synthetic.main.common_item_with_arrow.view.*
import kotlinx.android.synthetic.main.fragment_single_analysis.*


/**
 * 本fragment主要做一些公共请求事务，
 * 包括请求站点信息 选择站点时用到
 * 请求监测项目 在选择监测项目时用到 单站点分析时用到
 * 请求配置信息和组件信息 在多站点中用到
 * */
abstract class BaseBusinessFragment : BaseFragment(){

    companion object{
        const val SINGLE = 0
        const val MUTLI = 1
    }

    protected var mFragmentType: Int? = 0

    protected var mAirStations: ArrayList<TestPoint> = ArrayList()     //空气站数据
    protected var mSelectStation: TestPointDetail? = null       //选择的监测站点
    protected var mSelectCounty: CountyPoint? = null      //选择的区
    protected var mSelectCity: CityPoint? = null          //选择的城市
    protected var mSelectProvince: TestPoint? = null      //选择的省
    protected val mFactors: ArrayList<TabelHeaderData> = ArrayList()      //监测项目的类型 单站点
    protected var checkElems = ArrayList<String>()        //选择的监测项目
    protected var mComponentList = ArrayList<ComponentBean>()     //该界面需要监测的项目 多站点

    protected var mAllStations = ArrayList<TestPointDetail>()

    override fun initView() {
        mFragmentType = returnFragmentType()
    }

    override fun initData() {
        getAirStationTree()
        getReportConfigItem()
    }

    override fun initToolbar() {

    }

    abstract fun returnFragmentType() : Int

    /**
     * 站点信息
     * */
    private fun getAirStationTree() {
        Log.i(TAG, "getAirStationTree")
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
                                getAllStations()
                                if (mFragmentType == SINGLE){
                                    station.value.text = mSelectStation?.title
                                }
                                getTableHeaderInfo()
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 站点信息获取失败")
                    }
                })
    }

    /**
     * 保存所有站点，在多站点趋势分析中使用
     * */
    private fun getAllStations(){
        for (province in mAirStations){
            for (city in province.children!!){
                for (county in city.children!!){
                    for (station in county.children!!){
                        mAllStations.add(station)
                    }
                }
            }
        }
    }

    /**
     * 获取头部监测信息
     * */
    private fun getTableHeaderInfo(){
        Log.i(TAG, "initData: initTableHeaderInfo")
        val conditionParams = HashMap<String, Any>()
        conditionParams[AirConstants.ID] = mSelectStation?.key.toString()
        conditionParams[AirConstants.POLLUTIONTYPE] = "air"
        val params = HashMap<String, String>()
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        params[AirConstants.TIMELINESS] = "historical"
        OkHttpUtils.getInstance()
                .post(mActivity, HttpUtil.getAirServiceAddress(AirConstants.GET_TABLE_HEADER_DATA)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<TabelHeaderData>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<TabelHeaderData>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            if (d?.data != null){
                                Log.i(TAG, "onSuccess: 获取factors成功")
                                mFactors.clear()
                                mFactors.addAll(d.data)
                                for (factor in mFactors){
                                    Log.i(TAG, "onSuccess: 单站点 factorName = ${factor.ComponentID}")
                                }
                            }
                        }
                    }
                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 头部信息获取失败")
                    }
                })
    }

    /**
     * 配置信息
     * */
    private fun getReportConfigItem(){
        Log.i(TAG, "getReportConfigItem: ")
        val params = HashMap<String, String>()
        val conditionParams = HashMap<String, Any>()
        conditionParams["Type"] = "air-syntheticalCompare"
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        OkHttpUtils.getInstance()
                .post(mActivity, HttpUtil.getAirServiceAddress(AirConstants.GET_REPORT_CONFIG)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<ConfigItem>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<ConfigItem>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            Log.i(TAG, "onSuccess: 获取配置成功 再获取组件")
                            if (d?.data != null){
                                checkElems = d.data[0].CheckElems as ArrayList<String>
                                getComponentInfo()
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 配置信息获取失败")
                    }
                })
    }

    /**
     * 获取组件信息
     * */
    private fun getComponentInfo(){
        Log.i(TAG, "getComponentInfo: ")
        val params = HashMap<String, String>()
        val conditionParams = HashMap<String, Any>()
        conditionParams["Type"] = "air"
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        OkHttpUtils.getInstance()
                .post(mActivity, HttpUtil.getAirServiceAddress(AirConstants.GET_COMPONENT_INFO)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<ComponentBean>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<ComponentBean>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            Log.i(TAG, "onSuccess: 获取组件成功 整合出污染物项目")
                            for (component in d?.data!!){
                                for (check in checkElems){
                                    if (check == component.ComponentID){
                                        mComponentList.add(component)
                                    }
                                }
                            }
                            Log.i(TAG, "onSuccess: 组件 size = ${mComponentList.size} ")
                            Log.i(TAG, "onSuccess: mComponentList = $mComponentList")
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 组件信息获取失败")
                    }
                })
    }
}