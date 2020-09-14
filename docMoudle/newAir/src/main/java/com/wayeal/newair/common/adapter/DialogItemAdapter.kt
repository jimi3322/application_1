package com.wayeal.newair.common.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wayeal.newair.R
import kotlinx.android.synthetic.main.dialog_item.view.*

/**
 * 列表dialog适配器
 * */
class DialogItemAdapter(private val mContext: Context)
    : RecyclerView.Adapter<DialogItemAdapter.TitleViewHolder>(){

    private var mDataList: MutableList<String>? = null
    private var mOnItemClick: OnItemClickListener? = null

    fun setData(data: ArrayList<String>) {
        this.mDataList = data
        notifyDataSetChanged()
    }

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvItem: TextView? = null
        var divider: View? = null
        init {
            tvItem = itemView.item
            divider = itemView.divider
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        return TitleViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val item = mDataList?.get(position)
        holder.tvItem?.text = item
        if (mDataList?.size == position+1){
            holder.divider?.visibility = View.GONE
        }
        mOnItemClick?.let {
            holder.itemView?.setOnClickListener {
                mOnItemClick?.setOnclickItem(view = holder.itemView,position = position)
            }
        }
    }

    interface OnItemClickListener {
        fun setOnclickItem(view: View, position: Int)
    }

    fun setOnItemClickListener(onItemOnclick: OnItemClickListener) {
        this.mOnItemClick = onItemOnclick
    }

}