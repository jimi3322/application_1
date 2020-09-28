package com.app.yun.fragment

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.myapplication.basemvp.BaseFragment
import com.app.myapplication.widget.LinearItemDecoration
import com.app.yun.R
import com.app.yun.adpter.SystemLeftAdapter
import com.app.yun.adpter.SystemRightAdapter
import com.app.yun.bean.SystemResult
import com.chad.library.adapter.base.BaseQuickAdapter


class FourthFragment : BaseFragment(){
    private var leftRecyclerView: RecyclerView? = null
    private var rightRecyclerView: RecyclerView? = null
    private var systemLeftAdapter: SystemLeftAdapter? = null
    private var systemRightAdapter: SystemRightAdapter? = null
    private var leftCurPosition = 0
    private var list : ArrayList<SystemResult.ChildrenBean> = ArrayList()
    private var list2 : ArrayList<SystemResult.ChildrenBean> = ArrayList()
    private var systemResults : ArrayList<SystemResult> = ArrayList()


    override fun getLayoutResId(): Int {
        return R.layout.fragment_system
    }

    override fun initView(rootView: View) {
        leftRecyclerView = rootView.findViewById(R.id.rv_system_left)
        rightRecyclerView = rootView.findViewById(R.id.rv_system_right)
    }

    override fun initData() {
        leftRecyclerView?.setLayoutManager(
            LinearLayoutManager(
                mContext,
                LinearLayoutManager.VERTICAL, false
            )
        )
        rightRecyclerView?.setLayoutManager(GridLayoutManager(mContext, 2))
        // 设置 ItemDecoration 作为分割线
        val itemDecoration = LinearItemDecoration(mContext)
            .height(0.5f)    // dp
            .color(-0x55333334)  // color 的 int 值，不是 R.color 中的值
        leftRecyclerView?.addItemDecoration(itemDecoration)

        /**
         * 请求体系分类数据
         */
        list.add(SystemResult.ChildrenBean("内容1"))
        list2.add(SystemResult.ChildrenBean("内容2"))
        Log.i("list",":"+list)
        systemResults.add(SystemResult(1,"选项1", list))
        systemResults.add(SystemResult(1,"选项2", list2))
        Log.i("systemResults1",":"+systemResults)
        systemList(systemResults)
    }



    fun systemList(systemResults: List<SystemResult>?) {
        if (systemResults == null) {
            return
        }
        if (systemLeftAdapter == null) {
            // 左侧 recyclerview

            systemLeftAdapter = SystemLeftAdapter(R.layout.item_system_left, systemResults)
            systemResults[0].isSelected(true)
            systemLeftAdapter?.notifyDataSetChanged()
            systemLeftAdapter?.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                if (leftCurPosition == position) {
                    return@OnItemClickListener
                }
                leftCurPosition = position
                for (i in systemResults.indices) {
                    val systemResult = systemResults[i]
                    systemResult.setSelected(i == leftCurPosition)
                }
                systemLeftAdapter!!.notifyDataSetChanged()
                val systemResult = systemResults[position]
                if (systemResult != null) {
                    val children = systemResult!!.getChildrens()
                    systemRightAdapter!!.setNewData(children)
                }
            })
            leftRecyclerView?.setAdapter(systemLeftAdapter)

        } else {
            systemLeftAdapter?.setNewData(systemResults)
        }
        // 右侧 recyclerview
        if (systemRightAdapter == null) {
            systemRightAdapter =
                SystemRightAdapter(R.layout.item_system_right, systemResults[0].getChildrens())
            systemRightAdapter?.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                gotoSystemArticleActivity(
                    systemRightAdapter!!.getData().get(position)
                )
            })
            rightRecyclerView?.setAdapter(systemRightAdapter)
        }
    }

    /**
     * 跳转到体系文章列表
     *
     * @param childrenBean
     */
    private fun gotoSystemArticleActivity(childrenBean: SystemResult.ChildrenBean) {
        Toast.makeText(context,"点击", Toast.LENGTH_SHORT).show()
    }

    fun showLoading() {
    }

    fun hideLoading() {
    }
}
