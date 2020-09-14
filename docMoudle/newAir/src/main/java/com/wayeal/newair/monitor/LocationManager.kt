package com.wayeal.newair.monitor

import android.content.Context
import android.util.Log
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption

/**
 * 利用百度地图SDK进行定位
 * */
class LocationManager private constructor() {

    private var mLocationClient: LocationClient? = null
    private var mLocationListener: BDLocationListener? = null
    private val TAG = "LocationManager"
    private var mContext: Context? = null

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LocationManager()
        }
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

    inner class LocationListener: BDLocationListener{
        override fun onReceiveLocation(bdLocation: BDLocation?) {
            Log.i(TAG, "onReceiveLocation: des = ${bdLocation?.locationDescribe}")
            Log.i(TAG, "onReceiveLocation: lat = ${bdLocation?.latitude}")
            Log.i(TAG, "onReceiveLocation: lng = ${bdLocation?.longitude}")
            Log.i(TAG, "onReceiveLocation: city = ${bdLocation?.city}")
        }
    }

}
