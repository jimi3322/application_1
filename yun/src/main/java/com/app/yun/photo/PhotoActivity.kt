package com.app.yun.photo

import com.app.yun.R
import com.app.yun.base.BaseActivity
import com.baidu.mapapi.map.*
import kotlinx.android.synthetic.main.activity_map.*


class PhotoActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_map
    }

    override fun initView() {
        initLocation()
        //初始化标记
    }

    override fun initToolbar() {}

    override fun initData() {}


    /**
     * 定位初始化
     */
    fun initLocation(){

    }

}