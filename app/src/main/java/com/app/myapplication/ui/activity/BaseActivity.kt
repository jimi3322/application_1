package com.app.myapplication.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.myapplication.R
import com.app.myapplication.common.ActivityCollector

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        ActivityCollector.addActivitis(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivitis(this)
    }
}
