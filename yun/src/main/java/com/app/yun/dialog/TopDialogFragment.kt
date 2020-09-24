package com.app.yun.dialog

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.app.yun.R



class TopDialogFragment : DialogFragment() {
    private var ll: LinearLayout? = null
    val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialogView = inflater.inflate(R.layout.dialog_top, container)
        ll = dialogView.findViewById(R.id.mView)
        val params = ll!!.getLayoutParams() as FrameLayout.LayoutParams
        params.topMargin = getStatusBarHeight(dialog!!.context)
        ll!!.setLayoutParams(params) //默认在最顶部，把状态栏部分留出来
        return dialogView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFullScreen) //dialog全屏
    }


    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        val windowParams = window!!.attributes
        windowParams.dimAmount = 0.0f//Dialog外边框透明
        window.setLayout(-1, -2) //高度自适应，宽度全屏
        windowParams.gravity = Gravity.TOP //在顶部显示
        windowParams.windowAnimations = R.style.TopDialogAnimation
        window.attributes = windowParams
    }

    /**
     * 获取状态栏高度（单位：px）
     */
    fun getStatusBarHeight(context: Context): Int {
        val resources = context.getResources()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resources.getDimensionPixelSize(resourceId) === 0) 60 else resources.getDimensionPixelSize(
            resourceId
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler.postDelayed(Runnable {
            run {
                dismiss()
            }
        },3000)
    }
}