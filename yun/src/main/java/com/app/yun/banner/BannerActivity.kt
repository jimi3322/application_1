package com.app.yun.banner

import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.Log
import com.app.yun.R
import com.app.yun.base.BaseActivity
import com.app.yun.bean.WeChatAuthorResult
import com.app.yun.gridviewpager.GridRecyclerAdapter
import com.app.yun.gridviewpager.GridViewPagerAdapter
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_banner.*



class BannerActivity : BaseActivity() {
    private var weChatAuthorResults : ArrayList<WeChatAuthorResult> = ArrayList()

    var imageUrls = listOf(
        "https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg",
        "https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg",
        "https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg",
        "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg",
        "https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg"
    )
    private val colors = intArrayOf(
        -0x13bf86, -0x54b844, -0xd6490a, -0x81a83e
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_banner
    }

    override fun initView() {
        //val headerView = LayoutInflater.from(mContext).inflate(R.layout.layout_home_header, null)

        //bannerImage加载
        bannerImage()
        //刷新页面组件
        refreshView()
        //图标列表的加载
        weChatAuthorResults?.add(WeChatAuthorResult(1,"图",1))
        weChatAuthorResults?.add(WeChatAuthorResult(2,"标",2))
        weChatAuthorResults?.add(WeChatAuthorResult(3,"列",3))
        weChatAuthorResults?.add(WeChatAuthorResult(4,"表",4))
        weChatAuthorResults?.add(WeChatAuthorResult(5,"@",5))
        gridViewPager()

        //搜索框监听
        tv_home_search.setOnClickListener {
            startActivity(Intent(this, gotoSearchActivity::class.java))
        }
    }

    override fun initToolbar() {}

    override fun initData() {

    }

    private fun refreshView(){
        srl_home?.setOnRefreshListener(OnRefreshListener { refreshlayout ->
            refreshlayout.finishRefresh(2000/*,false*/)//传入false表示刷新失败
        })
        srl_home?.setOnLoadMoreListener(OnLoadMoreListener { refreshlayout ->
            //presenter.getHomeArticles(page)
             refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
        })
    }

    private fun bannerImage(){
        var adapter = BannerImageAdapter(imageUrls)
        banner_home?.let {
            it.addBannerLifecycleObserver(this)
            it.setIndicator(CircleIndicator(this))
            it.setBannerRound(20f)
            it.adapter = adapter
        }
    }


    private fun gridViewPager(){
        gvp_viewpager.setAdapter(object :
            GridViewPagerAdapter<WeChatAuthorResult>(weChatAuthorResults) {
            override fun bindData(
                viewHolder: GridRecyclerAdapter<*>.ViewHolder?,
                weChatAuthorResult: WeChatAuthorResult?,
                position: Int
            ) {
                val shapeDrawable = ShapeDrawable()
                shapeDrawable.shape = OvalShape()
                Log.i("position % colors.size", ":" + colors[position])
                shapeDrawable.paint.color = colors[position]
                viewHolder?.setText(R.id.tv_home_author_icon,"${weChatAuthorResult?.name}")
                    ?.setText(R.id.tv_home_author_name, weChatAuthorResult?.name)
                    ?.setBackground(R.id.tv_home_author_icon, shapeDrawable)
            }

        })
    }

}
