@file:Suppress("DEPRECATION")

package com.app.yun.dialog

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

abstract class PicBaseActivity : AppCompatActivity(){


    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initImmersion()
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentLayout()

    }

    open fun setContentLayout() {
        setContentView(getLayoutId())
        initView()
        initData()
    }

    /**
     * 解决键盘点击空白处收回的问题
     * */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)?.hideSoftInputFromWindow(v?.windowToken, 0)
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    open fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    open fun initData() {

    }

    open fun initImmersion(){

    }

    /**
     * 展示loading窗
     */
    protected open fun showLoadingView(msg: String?,isCancelable: Boolean=true) {
        //val msg = msg

        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, "", msg, true)
            progressDialog?.setCancelable(isCancelable)
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