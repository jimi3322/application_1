package com.app.yun.banner

import android.content.Context
import com.app.yun.R
import com.app.yun.base.BaseActivity
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_banner.*

class BannerActivity : BaseActivity() {
    private val mContext: Context? = null

    var imageUrls = listOf(
        "https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg",
        "https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg",
        "https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg",
        "https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg",
        "https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg"
    )

    override fun getLayoutId(): Int {
        return R.layout.activity_banner
    }

    override fun initView() {
        //val headerView = LayoutInflater.from(mContext).inflate(R.layout.layout_home_header, null)

        var adapter = BannerImageAdapter(imageUrls)
        banner_home?.let {
            it.addBannerLifecycleObserver(this)
            it.setIndicator(CircleIndicator(this))
            it.setBannerRound(20f)
            it.adapter = adapter
        }

        refreshView()
    }

    override fun initToolbar() {}

    override fun initData() {}

    fun refreshView(){
        srl_home?.setOnRefreshListener(OnRefreshListener { refreshlayout ->
            refreshlayout.finishRefresh(2000/*,false*/)//传入false表示刷新失败
        })
        srl_home?.setOnLoadMoreListener(OnLoadMoreListener { refreshlayout ->
            //presenter.getHomeArticles(page)
             refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
        })
    }

}
