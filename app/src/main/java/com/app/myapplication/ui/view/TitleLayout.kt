package com.app.myapplication.ui.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.app.myapplication.R
import com.app.myapplication.ui.activity.MenuActivity
import kotlinx.android.synthetic.main.title.view.*
import java.util.jar.Attributes

class TitleLayout(context: Context,attrs: AttributeSet): LinearLayout(context,attrs){
    init {
        LayoutInflater.from(context).inflate(R.layout.title,this)
        title_back.setOnClickListener{
            val activity = context as Activity
                activity.finish()
        }
        title_menu.setOnClickListener {

        }
    }
}