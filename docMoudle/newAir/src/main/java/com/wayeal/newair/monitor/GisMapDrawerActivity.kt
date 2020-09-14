package com.wayeal.newair.monitor

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import com.baidu.location.*
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.mylhyl.acp.Acp
import com.mylhyl.acp.AcpListener
import com.mylhyl.acp.AcpOptions
import com.wayeal.newair.R
import com.wayeal.newair.base.BaseActivity
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.SpaceItemDecoration
import com.wayeal.newair.common.view.CommonDialog
import com.wayeal.newair.monitor.adapter.LegndAdapter
import com.wayeal.newair.monitor.adapter.PollutionAdapter
import com.wayeal.newair.monitor.bean.CityPoint
import com.wayeal.newair.monitor.bean.LocationAirInfo
import com.wayeal.newair.monitor.bean.TestPoint
import kotlinx.android.synthetic.main.activity_gis_map.*
import kotlinx.android.synthetic.main.dialog_navigation.*
import kotlinx.android.synthetic.main.drawer_gis_map.*
import kotlinx.android.synthetic.main.info_window_map.view.*
import java.io.File


/**
 * GIS地图drawer部分
 * */

abstract class GisMapDrawerActivity : BaseActivity(){

    protected var mLegndAdapter: LegndAdapter? = null
    protected var mPollutionAdapter: PollutionAdapter? = null
    protected lateinit var mBaiduMap: BaiduMap

    protected var mSelectCity: CityPoint? = null          //选择的城市
    protected var mSelectProvince: TestPoint? = null      //选择的省
    protected var mProvinces: ArrayList<TestPoint> = ArrayList()        //获取的站点信息

    private var mLocationClient: LocationClient? = null
    private var mLocationListener: BDLocationListener? = null
    private val TAG = "LocationManager"
    private var mContext: Context? = null

    override fun initIntentData() {

    }

    override fun initView() {
        initDrawerView()
        initMapView()
    }

    private fun initDrawerView(){
        filter.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }
        //图例
        val layoutManager = LinearLayoutManager(this)
        picture_legnd.layoutManager = layoutManager
        mLegndAdapter = LegndAdapter(this)
        picture_legnd.adapter = mLegndAdapter
        picture_legnd.addItemDecoration(SpaceItemDecoration(bottom = AirUtils.dip2px(15f)))
        //污染物项目
        val grid = GridLayoutManager(this,2)
        pollution.layoutManager = grid
        mPollutionAdapter = PollutionAdapter(this)
        pollution.adapter = mPollutionAdapter
        pollution.addItemDecoration(SpaceItemDecoration(left = AirUtils.dip2px(5f)
                ,right = AirUtils.dip2px(5f)
                ,top = AirUtils.dip2px(15f)))
    }

    private fun initMapView(){
        mBaiduMap = bMapView.map
        bMapView.showZoomControls(false)
        mBaiduMap.mapType = BaiduMap.MAP_TYPE_NORMAL
        val builder: MapStatus.Builder = MapStatus.Builder()
        builder.zoom(13f)
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
        mBaiduMap.setOnMarkerClickListener { marker ->
            if (marker.extraInfo != null) {
                addPopWindow(marker.extraInfo)
            }
            false
        }
        mBaiduMap.setOnMapClickListener(object : BaiduMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng) {
                mBaiduMap.hideInfoWindow()
            }

            override fun onMapPoiClick(mapPoi: MapPoi): Boolean {
                return false
            }
        })
    }

    private fun addPopWindow(bundle: Bundle){
        Log.i(TAG, "addPopWindow: ")
        val view = layoutInflater.inflate(R.layout.info_window_map,null)
        val mLocationInfo = bundle.getSerializable(POLLUTION) as LocationAirInfo
        val latLng = LatLng(mLocationInfo.Lat.toDouble(),mLocationInfo.Lng.toDouble())
        view.findViewById<TextView>(R.id.name).text = (bundle.getSerializable(POLLUTION) as LocationAirInfo).Name
        view.updateTime.text = mLocationInfo.DataUpdateTime
        view.aqiValue.text = mLocationInfo.AQI
        view.aqiLevel.text = mLocationInfo.StatusName
        view.mainPollution.text = mLocationInfo.PrimaryPollutant
        //进行导航
        view.navigation.setOnClickListener {
            showNavigationDialog(mLocationInfo)
        }
        val mInfoWindow = InfoWindow(view,latLng,-100)
        mBaiduMap.showInfoWindow(mInfoWindow)
    }

    private fun showNavigationDialog(locationInfo:LocationAirInfo){
        val dialog = Dialog(this,R.style.DialogTheme)
        val inflate = LayoutInflater.from(this).inflate(R.layout.dialog_navigation, null)
        dialog.setContentView(inflate)
        val dialogWindow = dialog.window
        dialogWindow?.setGravity( Gravity.BOTTOM)
        dialogWindow?.setWindowAnimations(R.style.DialogAnimation)
        dialogWindow?.decorView?.setPadding(0, 0, 0, 0)
        val lp = dialogWindow?.attributes
        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
        lp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialogWindow?.attributes = lp
        dialog.run {
            lp?.width = WindowManager.LayoutParams.MATCH_PARENT
            lp?.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialogWindow?.attributes = lp
            show()
        }
        dialog.gaode.setOnClickListener { toGaoDeMap(locationInfo)
            dialog.dismiss()}
        dialog.tenxun.setOnClickListener { toTenXunMap(locationInfo)
            dialog.dismiss()}
        dialog.baidu.setOnClickListener {
            dialog.dismiss()
            toBaiDuMap(locationInfo)
        }
        dialog.cancel.setOnClickListener { dialog.dismiss() }
    }

    //百度地图
    private fun toBaiDuMap(locationInfo:LocationAirInfo) {
        if (File("/data/data/com.baidu.BaiduMap").exists()){
            val i1 = Intent()
            i1.data =
                    Uri.parse("baidumap://map/direction?" +
                            "region=beijing" +
                            "&destination=${locationInfo.Name}|${locationInfo.Lat},${locationInfo.Lng}" +
                            "&coord_type=bd09ll&mode=driving&src=andr.baidu.openAPIdemo")
            startActivity(i1)

        }else{
            upDataApk("http://gdown.baidu.com/data/wisegame/e0d6bf671e43072b/9157e0d6bf671e43072b742b1b80ef7d.apk")
        }
    }

    //腾讯地图
    @SuppressLint("SdCardPath")
    private fun toTenXunMap(locationInfo:LocationAirInfo) {
        if (File("/data/data/com.tencent.map").exists()) {
            val intent = Intent(
                    "android.intent.action.VIEW",
                    Uri.parse("qqmap://map/routeplan?" +
                            "type=drive&" +
                            "fromcoord=CurrentLocation&" +
                            "to=${locationInfo.Name}&" +
                            "tocoord=${locationInfo.Lat},${locationInfo.Lng}&" +
                            "referer=PZOBZ-F2W3J-GH7FR-KUKPT-QCKU6-IEFZR")
            )
            startActivity(intent)
        } else {
            upDataApk("https://qqmap-1251316161.file.myqcloud.com/mapCosApiUpload/apk/8.15.2.740/tencentmap_8.15.2.740_android_00002.apk")
        }
    }

    // 高德地图
    @SuppressLint("SdCardPath")
    private fun toGaoDeMap(locationInfo:LocationAirInfo) {
        if (File("/data/data/com.autonavi.minimap").exists()) {
            val intent = Intent(
                    "android.intent.action.VIEW",
                    Uri.parse("amapuri://route/plan/?dlat=" +
                            "${locationInfo.Lat}" +
                            "&dlon=" + "${locationInfo.Lng}" +
                            "&dname=" + locationInfo.Address +
                            "&dev=0&t=0")
            )
            startActivity(intent)
        } else {
            upDataApk("http://182.86.84.235/down6/C3060/Amap_V10.50.0.2522_android_C3060_(Build2006012154).apk?ali_redirect_domain=amapdownload.autonavi.com&ali_redirect_ex_ftag=ca869e879acd512ad745a84b327777198e03065b0e8900ee&ali_redirect_ex_tmining_ts=1594114323&ali_redirect_ex_tmining_expire=3600&ali_redirect_ex_hot=0")
        }
    }

    private fun upDataApk(uri:String) {
        CommonDialog.Builder(this)
                .setMessage("您还未安装此app是否前往下载")
                .setNegativeButton("取消", DialogInterface.OnClickListener{ p0, _ -> p0.dismiss()
                })
                .setMessageColor(Color.BLACK)
                .setPositiveButton("确定", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                    intent.data = Uri.parse(uri)
                    startActivity(intent)
                })
                .setWith(0.8f)
                .create()
                .show()
    }

    protected fun locationCity(cityLatlng: LatLng){
        Log.i(TAG, "locationCity: 移动位置")
        val mMapStatus = MapStatus.Builder()
                .target(cityLatlng)
                .zoom(12f)
                .build()
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus))
    }

    /**
     * 给city添加经纬度 就按第一个station来进行
     * */
    protected fun addCityLatlng(){
        Log.i(TAG, "addCityLatlng: 给city添加经纬度")
        for (indexProvince in mProvinces.indices){
            for (indexCity in mProvinces[indexProvince].children?.indices!!){
                mProvinces[indexProvince].children?.get(indexCity)?.latitude =
                        mProvinces[indexProvince].children?.get(indexCity)?.children?.get(0)?.children?.get(0)?.latitude
                mProvinces[indexProvince].children?.get(indexCity)?.longitude =
                        mProvinces[indexProvince].children?.get(indexCity)?.children?.get(0)?.children?.get(0)?.longitude
            }
        }
    }

    /**
     * 初始化location client
     * */
    protected fun initLocationClient(){
        Acp.getInstance(this).request(AcpOptions.Builder().setPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
        ).build(),object : AcpListener{
            override fun onGranted() {
                Log.i(TAG, "onGranted: 有权限")
                initLocation(this@GisMapDrawerActivity)
            }

            override fun onDenied(permissions: MutableList<String>?) {
                Log.i(TAG, "onDenied: 没有定位权限")
            }
        })
    }

    fun initLocation(context: Context){
        mContext = context
        if (mLocationClient == null){
            mLocationClient = LocationClient(context)
            mLocationListener = LocationListener()
            val option = LocationClientOption()
            option.apply {
                locationMode = LocationClientOption.LocationMode.Hight_Accuracy
                setIsNeedAddress(true)
                setIsNeedLocationDescribe(true)
                setNeedDeviceDirect(false);
                setIsNeedLocationPoiList(true)
                openGps = true
            }
            mLocationClient?.locOption = option
            mLocationClient?.registerLocationListener(mLocationListener)
        }
        if (!mLocationClient?.isStarted!!){
            mLocationClient?.start()
        }
    }

    protected var mBDLocation: BDLocation? = null

    inner class LocationListener: BDLocationListener {
        override fun onReceiveLocation(bdLocation: BDLocation?) {
            Log.i(TAG, "onReceiveLocation: des = ${bdLocation?.locationDescribe}")
            Log.i(TAG, "onReceiveLocation: lat = ${bdLocation?.latitude}")
            Log.i(TAG, "onReceiveLocation: lng = ${bdLocation?.longitude}")
            Log.i(TAG, "onReceiveLocation: city = ${bdLocation?.city}")
            mBDLocation = bdLocation
            city.text = bdLocation?.city
            bdLocation?.latitude?.toDouble()?.let { LatLng(it,bdLocation.longitude.toDouble()) }?.let { locationCity(it) }
        }
    }

}