package com.wayeal.newair.monitor

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.google.gson.Gson
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.common.dialog.ChoosePointDialog
import com.wayeal.newair.monitor.adapter.PollutionAdapter
import com.wayeal.newair.monitor.bean.*
import kotlinx.android.synthetic.main.activity_gis_map.*
import kotlinx.android.synthetic.main.activity_real_time_data.toolbar
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*

const val POLLUTION_SITE_ADDRESS = "address"
const val POLLUTION_SITE_NAME = "name"
const val POLLUTION_SITE_LATLNG = "latlng"
const val POLLUTION = "pollution"

class GisMapActivity : GisMapDrawerActivity() {

    private val TAG = "GisMapActivity"

    private var mPollutionList = ArrayList<ComponentBean>()         //污染物项目

    private var mStandardList = ArrayList<StandardInfo>()

    private var mSelectCompont:String? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_gis_map
    }

    override fun initView() {
        super.initView()
        //定位
        myLocation.setOnClickListener {
            city.text = mBDLocation?.city
            mBDLocation?.latitude?.let { mBDLocation?.longitude?.let { it1 -> LatLng(it, it1) } }?.let { locationCity(it) }
        }

        ll_city.setOnClickListener {
            Log.i(TAG, "initView: ")
            val selectCityDialog = ChoosePointDialog(this
                    , getString(R.string.tip)
                    , mProvinces
                    , mSelectProvince
                    , mSelectCity
                    , null
                    , null
                    , object : ChoosePointDialog.ConfirmPointListener {
                @SuppressLint("SetTextI18n")
                override fun onConfirm(selectProvince: TestPoint?,
                                       selectCity: CityPoint?,
                                       selectCounty: CountyPoint?,
                                       selectStation: TestPointDetail?) {
                    mSelectProvince = selectProvince
                    mSelectCity = selectCity
                    city.text = mSelectCity?.title
                    Log.i(TAG, "onConfirm: $mSelectProvince $mSelectCity")
                    mSelectCity?.latitude?.toDouble()?.let { mSelectCity!!.longitude?.toDouble()?.let { it1 -> LatLng(it, it1) } }?.let { locationCity(it) }
                }

                override fun onCancel() {

                }
            },true)
            selectCityDialog.show()
        }

        mPollutionAdapter?.setOnItemClickListener(object : PollutionAdapter.OnItemClickListener{
            @SuppressLint("RtlHardcoded")
            override fun setOnclickItem(position: Int) {
                Log.i(TAG, "setOnclickItem: 选中pos = ${mPollutionList[position]}")
                mPollutionAdapter?.setSelectPos(position)
                mSelectCompont = mPollutionList[position].ComponentID
                getMapLocationInfo()
                drawerLayout.openDrawer(Gravity.LEFT)
            }
        })
    }

    override fun initToolbar() {
        toolbar.title.text = resources.getString(R.string.gis_map)
        toolbar.back.visibility = View.VISIBLE
        toolbar.back.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
        initLocationClient()
        getAirStationTree()
    }

    private fun getAirStationTree() {
        val params = HashMap<String, String>()
        params[AirConstants.TYPE] = ""
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_AIR_STATION_TREE)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<TestPoint>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<TestPoint>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            mProvinces = d?.data!!
                            addCityLatlng()
                            mSelectProvince = mProvinces[0]
                            mSelectCity = mSelectProvince?.children?.get(0)
                            Log.i(TAG, "onSuccess: 站点市 = ${mSelectCity?.title}")
                            city.text = mSelectCity?.title
                            Log.i(TAG, "onSuccess: 定位到 ${mSelectCity?.title} 其中lat lng = ${mSelectCity?.latitude} ${mSelectCity?.longitude}")
                            mSelectCity?.latitude?.toDouble()?.let { mSelectCity!!.longitude?.toDouble()?.let { it1 -> LatLng(it, it1) } }?.let { locationCity(it) }

                            getStandardInfo()
                            getReportConfigItem()
                            getMapLocationInfo()
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Toast.makeText(this@GisMapActivity
                                , this@GisMapActivity.resources.getString(R.string.get_stations_fail)
                                , Toast.LENGTH_SHORT).show()
                    }
                })
    }


    /**
     * 获取空气标准
     * */
    private fun getStandardInfo() {
        Log.i(TAG, "getStandardInfo: ")
        val params = HashMap<String, String>()
        val conditionParams = HashMap<String, Any>()
        conditionParams[AirConstants.SETTING_TYPE] = "air-quality"
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_STANDARD_INFO)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<StandardInfo>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<StandardInfo>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            Log.i(TAG, "onSuccess: getStandardInfo")
                            if (d?.data != null) {
                                mStandardList = d.data
                                mLegndAdapter?.setData(mStandardList)
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: getStandardInfo")
                    }
                })
    }

    private var checkElems = ArrayList<String>()

    /**
     * 获取Config
     * */
    private fun getReportConfigItem() {
        Log.i(TAG, "getReportConfigItem: ")
        val params = HashMap<String, String>()
        val conditionParams = HashMap<String, Any>()
        conditionParams["Type"] = "air-GIS"
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_REPORT_CONFIG)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<ConfigItem>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<ConfigItem>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            Log.i(TAG, "onSuccess: getReportConfigItem")
                            if (d?.data != null) {
                                checkElems = d.data[0].CheckElems as ArrayList<String>
                                getComponentInfo()
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: getReportConfigItem")
                    }
                })
    }

    /**
     * 获取组件信息
     * */
    private fun getComponentInfo() {
        Log.i(TAG, "getReportConfigItem: ")
        val params = HashMap<String, String>()
        val conditionParams = HashMap<String, Any>()
        conditionParams["Type"] = "air"
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_COMPONENT_INFO)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<ComponentBean>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<ComponentBean>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            mPollutionList.add(ComponentBean(ComponentID = "AQI"
                                    , Description = "AQI"
                                    , Unit = ""
                                    , CreateTime = ""
                                    , Type = ""
                                    , Value = ""))
                            for (component in d?.data!!) {
                                for (check in checkElems) {
                                    if (check == component.ComponentID) {
                                        mPollutionList.add(component)
                                    }
                                }
                            }
                            Log.i(TAG, "onSuccess: 污染物项目 size = ${mPollutionList.size}")
                            mPollutionAdapter?.setData(mPollutionList)
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 组件信息获取失败")
                    }
                })
    }

    /**
     * 获取地图位置信息
     * */
    private fun getMapLocationInfo() {
        Log.i(TAG, "getMapLocationInfo: ")
        val params = HashMap<String, String>()
        params[AirConstants.TYPE] = "air"
        if (mSelectCompont != null  && mSelectCompont != "AQI"){
            val componentList = ArrayList<String>()
            componentList.add(mSelectCompont!!)
            params[AirConstants.COMPONENTS] = Gson().toJson(componentList)
        }
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_MAP_LOCATION_INFO)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<LocationAirInfo>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<LocationAirInfo>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS) {
                            Log.i(TAG, "onSuccess: 获取地图信息成功")
                            if (d?.data != null) {
                                addMarker(d.data)
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 地图信息获取失败")
                    }
                })
    }

    /**
     * 添加marker
     * */
    private fun addMarker(enterInfos: ArrayList<LocationAirInfo>) {
        Log.i(TAG, "AddMarker: size = ${enterInfos.size}")
        if (enterInfos.size <= 0) {
            return
        }
        mBaiduMap.clear()
        for (info in enterInfos) {
            val bd: BitmapDescriptor = getBitmapDescriptorByEntity(info)
            val ll = LatLng(info.Lat.toDouble(), info.Lng.toDouble())
            val bundle = Bundle()
            bundle.putString(POLLUTION_SITE_ADDRESS, info.Address)
            bundle.putString(POLLUTION_SITE_NAME, info.Name)
            bundle.putParcelable(POLLUTION_SITE_LATLNG, ll)
            bundle.putSerializable(POLLUTION,info)

            val options: OverlayOptions = MarkerOptions()
                    .position(ll)
                    .icon(bd)
                    .animateType(MarkerOptions.MarkerAnimateType.grow)
                    .extraInfo(bundle)
            mBaiduMap.addOverlay(options)
            bd.recycle()
        }
    }

    /**
     * 根据标记实体获取对应的位图
     *
     * @param info 实体信息
     * @return 位图
     */
    private fun getBitmapDescriptorByEntity(info: LocationAirInfo): BitmapDescriptor {
        val view = ImageView(this)
        info.StatusName?.let {
            when(info.StatusName){
                AirConstants.EXCELLENT_TEXT -> view.setImageResource(R.mipmap.wy_map_icon_excellent)
                AirConstants.GOOD_TEXT -> view.setImageResource(R.mipmap.wy_map_icon_good)
                AirConstants.SLIGHTLY_POLLUTED_TEXT -> view.setImageResource(R.mipmap.wy_map_icon_slightly_polluted)
                AirConstants.MIDDLE_POLLUTED_TEXT -> view.setImageResource(R.mipmap.wy_map_icon_middle_polluted)
                AirConstants.HEAVY_POLLUTED_TEXT -> view.setImageResource(R.mipmap.wy_map_icon_heavy_polluted)
                AirConstants.SERVE_POLLUTED_TEXT -> view.setImageResource(R.mipmap.wy_map_icon_serve_polluted)
                else -> view.setImageResource(R.mipmap.wy_map_icon_nodata)
            }
        }
        if (info.StatusName == null){
            view.setImageResource(R.mipmap.wy_map_icon_nodata)
        }
        return BitmapDescriptorFactory.fromView(view)
    }

    override fun onResume() {
        super.onResume()
        bMapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        bMapView.onDestroy()
    }

}