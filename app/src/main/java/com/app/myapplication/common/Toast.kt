package com.app.myapplication.common

import android.content.Context
import android.widget.Toast


//Toast的工具类
fun String.showToast(context: Context){
    Toast.makeText(context,this,Toast.LENGTH_SHORT).show()
}
fun Int.showToast(context: Context){
    Toast.makeText(context,this,Toast.LENGTH_SHORT).show()
}