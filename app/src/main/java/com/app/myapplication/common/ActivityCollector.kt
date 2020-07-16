package com.app.myapplication.common

import android.app.Activity

object ActivityCollector {
    private val activities = ArrayList<Activity>()

    fun addActivitis(activity: Activity){
        activities.add(activity)
    }
    fun removeActivitis(activity: Activity){
        activities.remove(activity)
    }
    fun finishAll(){
        for (activity in activities){
            if (!activity.isFinishing){
                activity.finish()
            }
        }
        activities.clear()
    }
}