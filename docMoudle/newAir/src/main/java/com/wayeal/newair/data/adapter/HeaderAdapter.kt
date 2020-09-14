package com.wayeal.newair.data.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wayeal.newair.R
import kotlinx.android.synthetic.main.item_header.view.*

/**
 * 列表dialog适配器
 * */
class HeaderAdapter(private val mContext: Context)
    : RecyclerView.Adapter<HeaderAdapter.TitleViewHolder>(){

    private var mDataList: MutableList<String>? = null
    private var mOnItemClick: OnItemClickListener? = null

    fun setData(data: ArrayList<String>) {
        this.mDataList = data
        notifyDataSetChanged()
    }


    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvheader: TextView? = null
        init {
            tvheader = itemView.header
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        return TitleViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_header, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val item = mDataList?.get(position)
        holder.tvheader?.text = item
    }

    interface OnItemClickListener {
        fun setOnclickItem(view: View, position: Int)
    }

    fun setOnItemClickListener(onItemOnclick: OnItemClickListener) {
        this.mOnItemClick = onItemOnclick
    }

}