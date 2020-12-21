package com.app.yun.dialog

import com.wayeal.thirdpartyoperation.view.Pic

object CommonDataManager {

    private var mImages:ArrayList<Pic>? = ArrayList()

    fun addImages(images:ArrayList<Pic>){
        mImages = null
        mImages = images
    }

    fun getImages():ArrayList<Pic>?{
        return mImages
    }

}