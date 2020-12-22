package com.app.yun.map

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.app.yun.R
import com.app.yun.base.BaseActivity
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_marker.*
import kotlinx.android.synthetic.main.activity_marker.mMapView


class MapActivity : BaseActivity() , SensorEventListener {
    private var mBaiduMap: BaiduMap? = null
    /*
     * 注意！！！：kotlin里面不要初始化，
     * private var mMapView : MapView? = null，因为定义为空导致后面赋值的mBaiduMap一直为空，所以图层一直不显示
     */

    // 定位相关
    private var mLocationClient: LocationClient? = null
    private val myListener = MyLocationListener()
    // 定位图层显示方式
    private var mCurrentMode: MyLocationConfiguration.LocationMode? = null
    private var mSensorManager: SensorManager? = null
    private var lastX: Double = 0.0
    private var mCurrentDirection = 0
    private var mCurrentLat = 0.0
    private var mCurrentLon = 0.0
    private var mCurrentAccracy: Float = 0.toFloat()
    // 是否开启定位图层
    private var isLocationLayerEnable = true
    private var myLocationData: MyLocationData? = null
    // 是否首次定位
    private var isFirstLoc = true
    //标记
    private val bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)

    override fun getLayoutId(): Int {
        return R.layout.activity_map
    }

    override fun initView() {
        // 地图初始化
        mBaiduMap = mMapView?.map
        Log.i("mBaiduMap##########",":"+mBaiduMap)

        // 获取传感器管理服务
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        //设置地图模式
        // 传入null，则为默认图标
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL
        mBaiduMap?.setMyLocationConfiguration(MyLocationConfiguration(mCurrentMode, true, null))
        // 为系统的方向传感器注册监听器
        mSensorManager!!.registerListener(
            this,
            mSensorManager!!.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_UI
        )
        // 定位初始化
        initLocation()
        //初始化标记
        initMark()
    }

    override fun initToolbar() {}

    override fun initData() {}

    override fun onResume() {
        super.onResume()
        // 在activity执行onResume时必须调用mMapView. onResume ()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        // 在activity执行onPause时必须调用mMapView. onPause ()
        mMapView?.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
        // 取消注册传感器监听
        mSensorManager?.unregisterListener(this)
        // 退出时销毁定位
        mLocationClient?.stop()
        // 关闭定位图层
        mBaiduMap?.setMyLocationEnabled(false)
        // 在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val x = sensorEvent.values[SensorManager.DATA_X].toDouble()
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = x.toInt()
            myLocationData = MyLocationData.Builder()
                .accuracy(mCurrentAccracy)// 设置定位数据的精度信息，单位：米
                .direction(mCurrentDirection.toFloat())// 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(mCurrentLat)
                .longitude(mCurrentLon)
                .build()
            mBaiduMap?.setMyLocationData(myLocationData)
        }
        lastX = x
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    /**
     * 定位初始化
     */
    fun initLocation(){
        // 开启定位图层
        mBaiduMap?.setMyLocationEnabled(true)
        //定位初始化
        mLocationClient = LocationClient(this)
        mLocationClient!!.registerLocationListener(myListener)
        //通过LocationClientOption设置LocationClient相关参数
        val option = LocationClientOption()
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(1000)
        option.setIsNeedAddress(true)// 可选，设置地址信息
        option.setIsNeedLocationDescribe(true)//可选，设置是否需要地址描述
        //设置locationClientOption
        mLocationClient!!.setLocOption(option)
        //开启地图定位图层
        mLocationClient!!.start()
    }

    /**
     * 初始化标记
     */
    fun initMark(){
        val ll = LatLng(39.963175,116.400244)
        val bundle = Bundle()
        bundle.putString("经纬度",ll.toString())
        val options: OverlayOptions = MarkerOptions()
            .position(ll)
            .icon(bitmap)
            .animateType(MarkerOptions.MarkerAnimateType.grow)
            .zIndex(10)// 设置 marker 覆盖物的 zIndex
            .draggable(true)  // 设置 marker 是否允许拖拽，默认不可拖拽
            .extraInfo(bundle)
        mBaiduMap?.addOverlay(options)
    }

    /**
     * 设置定位图层的开启和关闭
     */
    fun setLocEnable(v: View) {
        if (isLocationLayerEnable) {
            mBaiduMap?.setMyLocationEnabled(false)
            (v as Button).text = "开启定位图层"
            isLocationLayerEnable = !isLocationLayerEnable
        } else {
            mBaiduMap?.setMyLocationEnabled(true)
            mBaiduMap?.setMyLocationData(myLocationData)
            (v as Button).text = "关闭定位图层"
            isLocationLayerEnable = !isLocationLayerEnable
        }
    }

    //通过继承抽象类BDAbstractListener并重写其onReceieveLocation方法来获取定位数据，并将其传给MapView
    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null && mMapView == null) {
                return
            }
            mCurrentLat = location.latitude
            mCurrentLon = location.longitude
            mCurrentAccracy = location.radius
            myLocationData = MyLocationData.Builder()
                .accuracy(location.radius)// 设置定位数据的精度信息，单位：米
                .direction(mCurrentDirection.toFloat())// 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(location.latitude)
                .longitude(location.longitude)
                .build()
            mBaiduMap?.setMyLocationData(myLocationData)
            if (isFirstLoc) {
                isFirstLoc = false
                val ll = LatLng(location.latitude, location.longitude)
                Log.i("onReceiveLocation","ll:"+ll)
                val builder = MapStatus.Builder()
                    .target(ll)
                    .zoom(12.0f)
                    .build()
                mBaiduMap?.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder))
            }

            // 显示当前信息
            val stringBuilder = StringBuilder()
            stringBuilder.append("\n经度：" + location.latitude)
            stringBuilder.append("\n纬度：" + location.longitude)
            stringBuilder.append("\n状态码：" + location.locType)
            stringBuilder.append("\n国家：" + location.country)
            stringBuilder.append("\n城市：" + location.city)
            stringBuilder.append("\n区：" + location.district)
            stringBuilder.append("\n街道：" + location.street)
            stringBuilder.append("\n地址：" + location.addrStr)
            mtextView.setText(stringBuilder.toString())
        }
    }

}