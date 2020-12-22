package com.app.yun.tree

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.yun.R
import com.app.yun.base.BaseActivity
import kotlinx.android.synthetic.main.activity_tree_recyclerview.*
import java.util.ArrayList


class TreeRecyclerViewActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        Log.i("xq","getLayoutId")
        return R.layout.activity_tree_recyclerview
    }

    override fun initToolbar() {
        Log.i("xq","initToolbar")
    }

    override fun initView() {
        Log.i("xq","initView")
    }

    override fun initData() {
        Log.i("xq","initData")
        val treeAdapter = TreeAdapter(this, initList())
        rvTree.setLayoutManager(LinearLayoutManager(this))
        rvTree.setAdapter(treeAdapter)
    }

    private fun initList(): List<TreeItem> {
        val list = ArrayList<TreeItem>()

        val item_0_0 = TreeItem()
        item_0_0.title = "第0级，第0个"
        item_0_0.child = ArrayList<TreeItem>()

        val item_1_0 = TreeItem()
        item_1_0.title = "第1级，第0个"
        item_1_0.child = ArrayList<TreeItem>()
        val item_2_0 = TreeItem()
        item_2_0.title = "第2级，第0个"
        val item_2_1 = TreeItem()
        item_2_1.title = "第2级，第1个"
        val item_2_2 = TreeItem()
        item_2_2.title = "第2级，第2个"
        item_1_0.child.add(item_2_0)
        item_1_0.child.add(item_2_1)
        item_1_0.child.add(item_2_2)
        item_0_0.child.add(item_1_0)

        val item_1_1 = TreeItem()
        item_1_1.title = "第1级，第1个"
        item_1_1.child = ArrayList<TreeItem>()
        val item_2_3 = TreeItem()
        item_2_3.title = "第2级，第3个"
        val item_2_4 = TreeItem()
        item_2_4.title = "第2级，第4个"
        item_2_3.child = ArrayList<TreeItem>()
        val item_3_0 = TreeItem()
        item_3_0.title = "第3级，第0个"
        val item_3_1 = TreeItem()
        item_3_1.title = "第3级，第1个"
        val item_3_2 = TreeItem()
        item_3_2.title = "第3级，第2个"
        item_2_3.child.add(item_3_0)
        item_2_3.child.add(item_3_1)
        item_2_3.child.add(item_3_2)
        item_1_1.child.add(item_2_3)
        item_1_1.child.add(item_2_4)
        item_0_0.child.add(item_1_1)
        list.add(item_0_0)


        val item_0_1 = TreeItem()
        item_0_1.title = "第0级，第1个"
        item_0_1.child = ArrayList<TreeItem>()

        val item_1_2 = TreeItem()
        item_1_2.title = "第1级，第2个"
        item_1_2.child = ArrayList<TreeItem>()
        val item_2_5 = TreeItem()
        item_2_5.title = "第2级，第5个"
        val item_2_6 = TreeItem()
        item_2_6.title = "第2级，第6个"
        val item_2_7 = TreeItem()
        item_2_7.title = "第2级，第7个"
        item_1_2.child.add(item_2_5)
        item_1_2.child.add(item_2_6)
        item_1_2.child.add(item_2_7)
        item_0_1.child.add(item_1_2)

        val item_1_3 = TreeItem()
        item_1_3.title = "第1级，第3个"
        item_1_3.child = ArrayList<TreeItem>()
        val item_2_8 = TreeItem()
        item_2_8.title = "第2级，第8个"
        val item_2_9 = TreeItem()
        item_2_9.title = "第2级，第9个"
        item_2_8.child = ArrayList<TreeItem>()
        val item_3_3 = TreeItem()
        item_3_3.title = "第3级，第3个"
        val item_3_4 = TreeItem()
        item_3_4.title = "第3级，第4个"
        val item_3_5 = TreeItem()
        item_3_5.title = "第3级，第5个"
        item_2_8.child.add(item_3_3)
        item_2_8.child.add(item_3_4)
        item_2_8.child.add(item_3_5)
        item_1_3.child.add(item_2_8)
        item_1_3.child.add(item_2_9)
        item_0_1.child.add(item_1_3)
        list.add(item_0_1)

        return list
    }

}