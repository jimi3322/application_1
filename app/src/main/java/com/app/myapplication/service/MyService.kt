package com.app.myapplication.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.myapplication.MainActivity
import com.app.myapplication.R
import com.app.myapplication.ui.activity.ContactActivity
import com.app.myapplication.ui.activity.MenuActivity
import java.util.jar.Manifest

class MyService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("myService","onCreate executed")
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("my_service","前台Service通知",NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val intent = Intent(this,MenuActivity::class.java)//点击消息通知跳转的页面
        val pi = PendingIntent.getActivity(this,0,intent,0)
        val notification = NotificationCompat.Builder(this,"my_service")
            .setContentTitle("通知的标题")
            .setContentText("通知内容：点击即将跳转到菜单页")
            .setSmallIcon(R.drawable.phone)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.small))
            .setContentIntent(pi)
            .build()
        //这里没有使用NotificationManager,startForeground将MyService变成前台通知
        startForeground(1,notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
