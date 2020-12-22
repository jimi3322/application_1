package com.app.yun.tree

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.yun.R
import com.app.yun.tree.utils.ScreenUtils
import kotlinx.android.synthetic.main.item_recyclerview_tree.view.*
import java.util.LinkedList


/**
 * Created by KaelLi on 2018/11/26.
 */
class TreeAdapter(private val mContext: Context, list: List<TreeItem>) :
    RecyclerView.Adapter<TreeAdapter.ViewHolder>(), TreeStateChangeListener {
    private val mList: MutableList<TreeItem>

    init {
        initList(list, 0)
        this.mList = LinkedList()
        this.mList.addAll(list)
    }

    private fun initList(list: List<TreeItem>?, level: Int) {
        if (list == null || list.size <= 0) return
        for (item in list) {
            item.itemLevel = level
            if (item.child != null && item.child.size > 0) {
                initList(item.child, level + 1)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.item_recyclerview_tree,
                viewGroup,
                false
            )
        )
    }



    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val treeItem = mList[position]
        viewHolder.mTextView!!.text = treeItem.title
        if (position == mList.size - 1) {
            viewHolder.mDivider!!.visibility = View.VISIBLE
        } else if (mList[position + 1].itemLevel == 0) {
            viewHolder.mDivider!!.visibility = View.VISIBLE
        } else {
            viewHolder.mDivider!!.visibility = View.INVISIBLE
        }

        if (treeItem.child != null && treeItem.child.size > 0) {
            viewHolder.tvState!!.visibility = View.VISIBLE
            if (treeItem.itemState == ITEM_STATE_OPEN) {
                viewHolder.tvState!!.text = "-"
            } else {
                viewHolder.tvState!!.text = "+"
            }
        } else {
            viewHolder.tvState!!.visibility = View.INVISIBLE
        }

        viewHolder.mCheck!!.isChecked = treeItem.itemCheck != false


        /*val lp = viewHolder.mIndicator!!.layoutParams as ConstraintLayout.LayoutParams
        lp.width = (1080 * (0.044444 - treeItem.itemLevel * 0.011111)).toInt()
        lp.height = (1920 * (0.044444 - treeItem.itemLevel * 0.011111)).toInt()
        viewHolder.mIndicator!!.layoutParams = lp*/

        viewHolder.mIndicator?.setOnClickListener {
            if (treeItem.itemState == ITEM_STATE_CLOSE) {
                onOpen(treeItem, viewHolder.adapterPosition)
            } else {
                onClose(treeItem, viewHolder.adapterPosition)
            }
        }
        viewHolder.mCheck?.setOnClickListener {
            //选择
            treeItem.itemCheck = !treeItem.itemCheck
            checkChild(treeItem,treeItem.itemCheck)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onOpen(treeItem: TreeItem, position: Int) {
        if (treeItem.child != null && treeItem.child.size > 0) {
            mList.addAll(position + 1, treeItem.child)
            treeItem.itemState = ITEM_STATE_OPEN
            notifyItemRangeInserted(position + 1, treeItem.child.size)
            notifyItemChanged(position)
        }
    }

    override fun onClose(treeItem: TreeItem, position: Int) {
        if (treeItem.child != null && treeItem.child.size > 0) {
            var nextSameOrHigherLevelNodePosition = mList.size - 1
            if (mList.size > position + 1) {
                for (i in position + 1 until mList.size) {
                    if (mList[i].itemLevel <= mList[position].itemLevel) {
                        nextSameOrHigherLevelNodePosition = i - 1
                        break
                    }
                }
                closeChild(mList[position])
                if (nextSameOrHigherLevelNodePosition > position) {
                    mList.subList(position + 1, nextSameOrHigherLevelNodePosition + 1).clear()
                    treeItem.itemState = ITEM_STATE_CLOSE
                    notifyItemRangeRemoved(
                        position + 1,
                        nextSameOrHigherLevelNodePosition - position
                    )
                    notifyItemChanged(position)
                }
            }
        }
    }

    private fun closeChild(treeItem: TreeItem) {
        if (treeItem.child != null) {
            for (child in treeItem.child) {
                child.itemState = ITEM_STATE_CLOSE
                closeChild(child)
            }
        }
    }

    //递归调用
    private fun checkChild(treeItem: TreeItem,checkStaus: Boolean) {
        if (treeItem.child != null) {
            for (child in treeItem.child) {
                //父级选择的状态传递给子级
                if (checkStaus == false){
                    child.itemCheck = false
                }else{
                    child.itemCheck = true
                }
                checkChild(child,checkStaus)
            }
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mIndicator: View? = null
        var tvState: TextView? = null
        var mTextView: TextView? = null
        var mDivider: View? = null
        var mCheck: CheckBox? = null

        init {
            mIndicator = itemView.vIndicator
            tvState = itemView.tvState
            mTextView = itemView.tvTitle
            mDivider = itemView.vDivider
            mCheck = itemView.tvCheck
        }
    }

    companion object {
        private val ITEM_STATE_CLOSE = 0
        private val ITEM_STATE_OPEN = 1
    }
}
