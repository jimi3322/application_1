package com.app.yun.echart

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.app.yun.R
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        chartView.loadUrl("file:///android_asset/echart/biz/map.html")
    }

}
