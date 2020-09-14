package com.wayeal.newair.base

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.gyf.immersionbar.ImmersionBar
import com.wayeal.newair.R

abstract class BaseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("BaseActivity", "onCreate: ")
        ImmersionBar
                .with(this)
                .statusBarColor(R.color.airBlue)
                .statusBarDarkFont(true)
                .init()
        super.onCreate(savedInstanceState)
        setContentLayout()
    }

    open fun setContentLayout() {
        setContentView(getLayoutId())
        initIntentData()
        initToolbar()
        initView()
        initData()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    protected open fun initIntentData(){}

    abstract fun initToolbar()

    abstract fun initData()

    private fun initImmersion(){
        ImmersionBar
                .with(this)
                .statusBarColor("#5473ff")
                .statusBarDarkFont(true)
                .init()
    }

}