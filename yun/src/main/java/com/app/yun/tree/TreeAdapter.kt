package com.app.yun.tree

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val treeItem = mList[i]
        viewHolder.mTextView!!.text = treeItem.title
        if (i == mList.size - 1) {
            viewHolder.mDivider!!.visibility = View.VISIBLE
        } else if (mList[i + 1].itemLevel == 0) {
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

        val lp = viewHolder.mIndicator!!.layoutParams as ConstraintLayout.LayoutParams
        lp.width = (ScreenUtils.getScreenWidth() * (0.044444 - treeItem.itemLevel * 0.011111)).toInt()
        lp.height = (ScreenUtils.getScreenWidth() * (0.044444 - treeItem.itemLevel * 0.011111)).toInt()
        viewHolder.mIndicator!!.layoutParams = lp

        viewHolder.itemView.setOnClickListener {
            if (treeItem.itemState == ITEM_STATE_CLOSE) {
                onOpen(treeItem, viewHolder.adapterPosition)
            } else {
                onClose(treeItem, viewHolder.adapterPosition)
            }
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


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mIndicator: View? = null
        var tvState: TextView? = null
        var mTextView: TextView? = null
        var mDivider: View? = null

        init {
            mIndicator = itemView.vIndicator
            tvState = itemView.tvState
            mTextView = itemView.tvTitle
            mDivider = itemView.vDivider
        }
    }

    companion object {
        private val ITEM_STATE_CLOSE = 0
        private val ITEM_STATE_OPEN = 1
    }
}
