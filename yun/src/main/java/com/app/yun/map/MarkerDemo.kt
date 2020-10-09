package com.app.yun.map

import android.util.Log
import com.app.yun.R
import com.app.yun.base.BaseActivity
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import kotlinx.android.synthetic.main.activity_marker.*


/**
 * 介绍在地图上绘制Marker,添加动画，响应事件
 */

class MarkerDemo : BaseActivity() {
    // MapView 是地图主控件
    private var mBaiduMap: BaiduMap? = null
    private var mMarkerA: Marker? = null

    // 初始化全局 bitmap 信息，不用时及时 recycle
    private val bitmapA = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding)

    override fun getLayoutId(): Int {
        return R.layout.activity_marker
    }

    override fun initView() {
        mBaiduMap = mMapView.map
        Log.i("mBaiduMap$$$$$$$$",":"+mBaiduMap)
        val mapStatusUpdate = MapStatusUpdateFactory.zoomTo(13.0f)
        mBaiduMap!!.setMapStatus(mapStatusUpdate)

        initMarker()
    }

    override fun initToolbar() {
    }

    override fun initData() {
    }


    /*public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marker)

    }*/

    fun initMarker() {
        // add marker overlay
        val latLngA = LatLng(39.963175, 116.400244)
        val markerOptionsA = MarkerOptions()
            .position(latLngA)
            .icon(bitmapA)// 设置 Marker 覆盖物的图标
            .zIndex(9)// 设置 marker 覆盖物的 zIndex
            .draggable(true) // 设置 marker 是否允许拖拽，默认不可拖拽
            .animateType(MarkerOptions.MarkerAnimateType.grow)
        mMarkerA = mBaiduMap!!.addOverlay(markerOptionsA) as Marker
    }


    override fun onPause() {
        super.onPause()
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        // MapView的生命周期与Activity同步，当activity恢复时必须调用MapView.onResume()
        mMapView!!.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 回收bitmap资源，防止内存泄露
        bitmapA.recycle()
        // 清除所有图层
        mBaiduMap!!.clear()
        // MapView的生命周期与Activity同步，当activity销毁时必须调用MapView.destroy()
        mMapView!!.onDestroy()
    }
}
