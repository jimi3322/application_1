package com.app.yun.dialog

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.yun.R
import com.app.yun.base.BaseActivity
import com.github.chrisbanes.photoview.PhotoView

import com.wayeal.thirdpartyoperation.view.Pic
import kotlinx.android.synthetic.main.activity_show_picture.*
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*


class ShowPictureActivity : BaseActivity() {

    private val TAG = "ShowPictureActivity"

    companion object{
        const val CURRENT_POSITION = "currentPosition"
    }

    override fun initView() {
        initToolbar()
        initData()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_show_picture
    }

    class ImageAdapter(imageList:ArrayList<Pic>, activity:Activity) : PagerAdapter(){

        private val images = imageList
        private val mActivity = activity

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return images.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val pic = images[position]
            val photoView = PhotoView(mActivity)
            photoView.setImageBitmap(pic.bitmap)
            container.addView(photoView)
            return photoView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }

    }



    override fun initToolbar(){
        show_picture_toolbar.findViewById<ImageView>(R.id.common_blue_back).setOnClickListener {
            finish()
        }
        show_picture_toolbar.findViewById<TextView>(R.id.common_blue_title).text = "1/1"
        show_picture_toolbar.findViewById<ImageView>(R.id.common_blue_right_button).visibility = View.INVISIBLE
    }

    private var currentPosition: Int = 0
    private var imagesList: ArrayList<Pic>? = null
    private var adapter:ImageAdapter? = null

    override fun initData() {
        currentPosition = intent.extras?.getInt(CURRENT_POSITION,0) ?: 0
//        imagesList = intent.extras?.get(PIC_LIST) as ArrayList<Pic>
        imagesList = CommonDataManager.getImages()

        adapter = ImageAdapter(imagesList!!,this)
        vp_image.adapter = adapter
        vp_image.setCurrentItem(currentPosition!!,false)
        vp_image.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                Log.i(TAG, "onPageSelected: currentPos = $currentPosition size = ${imagesList?.size}")
                show_picture_toolbar.common_blue_title.text = "${(position + 1)}/${imagesList?.size}"
            }
        })
        show_picture_toolbar.common_blue_title.text = "${(currentPosition + 1)}/${imagesList?.size}"

    }

}