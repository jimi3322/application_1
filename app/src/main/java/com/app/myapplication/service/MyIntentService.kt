package com.app.myapplication.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyIntentService : IntentService("MyIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        //打印当前线程的ID
        Log.d("myIntentService","Thresd id is ${Thread.currentThread().name}")

    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("myIntentService","onDestroy executed")
    }

}
