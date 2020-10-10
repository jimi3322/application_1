package com.app.water.base

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar

abstract class BaseActivity : AppCompatActivity(){

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("BaseActivity", "onCreate: ")
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

    /**
     * 展示loading窗
     */
    protected open fun showLoadingView(msg: String?) {
        var msg = msg
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, "", msg, true)
            progressDialog?.setCancelable(true)
        } else {
            progressDialog?.setMessage(msg)
            try {
                progressDialog?.show()
            }catch (e: Exception){
                progressDialog?.dismiss()
            }
        }
    }

    /**
     * 隱藏loading窗
     */
    protected open fun dismissLoadingView() {
        if (progressDialog != null && progressDialog?.isShowing()!!) {
            progressDialog?.dismiss()
        }
    }

}