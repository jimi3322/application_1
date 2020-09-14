package com.wayeal.newair.common.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.wayeal.newair.R
import com.wayeal.newair.common.bean.TitleBean
import kotlinx.android.synthetic.main.common_title.view.*

/**
 * 标题适配器
 * */
class CommonTitleAdapter(private val mContext: Context) : RecyclerView.Adapter<CommonTitleAdapter.TitleViewHolder>(){

    private var mDataList: MutableList<TitleBean>? = null
    private var mOnItemClick: OnItemClickListener? = null

    fun setData(data: ArrayList<TitleBean>) {
        this.mDataList = data
        notifyDataSetChanged()
    }

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivImage: ImageView? = null
        var tvTitle: TextView? = null
        var tvTitleDes: TextView? = null

        init {
            ivImage = itemView.title_icon
            tvTitle = itemView.title
            tvTitleDes = itemView.title_des
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        return TitleViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.common_title, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val item = mDataList?.get(position)
        item?.let {
            holder.ivImage?.setImageResource(it.titleIcon)
            holder.tvTitle?.text = it.title
            holder.tvTitleDes?.text = it.titleDes
        }
        mOnItemClick?.let {
            holder.ivImage?.setOnClickListener {
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