package com.app.yun.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.app.yun.R
import kotlinx.android.synthetic.main.title_m_layout.view.*


class TitleLayout(context: Context, attrs: AttributeSet): LinearLayout(context,attrs){
    init {
        LayoutInflater.from(context).inflate(R.layout.title_m_layout,this)
        button_backward.setOnClickListener{
            val activity = context as Activity
            activity.finish()
        }


    }
}