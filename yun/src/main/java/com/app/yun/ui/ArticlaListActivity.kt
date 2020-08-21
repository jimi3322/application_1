package com.app.yun.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.myapplication.CommonBaseActivity
import com.app.myapplication.common.utils.http.GsonResponseHandler
import com.app.myapplication.common.utils.http.OkHttpUtils
import com.app.yun.R
import com.app.yun.adpter.ArticleAdapter
import com.app.yun.data.Artical
import com.app.yun.data.ArticleListData
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_articla_list.*


class ArticlaListActivity : CommonBaseActivity(){
    private var pages:Int = 0
    private var madapter: ArticleAdapter? = null
    private  var articleListDatas : ArrayList<Artical>? = null //可空

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articla_list)

        initView()
        articleList(pages)

        refreshArtical.setOnClickListener {
            initView()
            articleList(pages)
        }
    }
    private fun initView(){
        showLoadingView("加载数据等待")
        val layoutManager = LinearLayoutManager(this)                                                         //列表的布局方式
        //val layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)      //此为瀑布流布局方式
        articleListView.layoutManager = layoutManager

        refreshLayout.setRefreshHeader(ClassicsHeader(this));
        refreshLayout.setRefreshFooter(ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshlayout: RefreshLayout) {
                articleList(0)
                refreshlayout.finishRefresh(2000)//传入false表示刷新失败

            }
        })
        refreshLayout.setOnLoadMoreListener (object : OnLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pages+=1
                articleList(pages)
                refreshLayout.finishLoadMore(2000)
            }
        })

    }
    private fun articleList(pages:Int){
        val URL = "https://www.wanandroid.com/article/list/${pages}/json"
        OkHttpUtils.getInstance().get(
            this,
            URL,
            object : GsonResponseHandler<ArticleListData>(){
                override fun onSuccess(statusCode: Int, data: ArticleListData?) {
                    articleListDatas = data?.data?.datas!!
                    Log.d("pages1",":"+pages)
                    Log.d("pages2",":"+articleListDatas)
                    handleArticalData()
                }

                override fun onFailure(statusCode: Int, error_msg: String?) {
                    Log.d("onFailure",":"+error_msg)
                }
            })
    }

    private  fun handleArticalData(){
        //articleListDatas = null

        Log.d("articleListDatas1",":"+articleListDatas)
        if(articleListDatas == null){
            val content = SpannableString(resources.getString(R.string.artical_list_refresh))
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            refreshArtical.text = content

        }
        dismissLoadingView()
        //异步操作导致madapter在未获取数据之前已经加载所以是空的,使用notifyDataSetChanged进行数据更新
        madapter = ArticleAdapter(articleListDatas)
        articleListView.adapter = madapter
        //madapter?.notifyDataSetChanged()
    }
}
