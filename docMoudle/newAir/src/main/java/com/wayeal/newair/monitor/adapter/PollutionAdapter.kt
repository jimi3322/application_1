package com.wayeal.newair.monitor.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.wayeal.newair.R
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.monitor.bean.ComponentBean
import kotlinx.android.synthetic.main.item_pollution.view.*


class PollutionAdapter(private val mContext: Context) :
        RecyclerView.Adapter<PollutionAdapter.ViewHolder>() {

    private var mDataList:MutableList<ComponentBean>? = null
    private var mOnItemClick: OnItemClickListener? = null
    private var mSelected: Int = 0

    fun setData(data:ArrayList<ComponentBean>){
        this.mDataList = data
        notifyDataSetChanged()
    }

    fun setSelectPos(select: Int){
        mSelected = select
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_pollution, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList?.get(position)
        item?.let {
            if (mSelected == position){
//                holder.rlColor?.setBackgroundColor(mContext.resources.getColor(R.color.commonBlueColor))
                holder.rlColor?.setBackgroundDrawable(AirUtils.getRoundRectDrawable(AirUtils.dip2px(8f), Color.parseColor("#4CA7FF"),true,0))
            }else{
//                holder.rlColor?.setBackgroundColor(mContext.resources.getColor(R.color.CommonTextHint))
                holder.rlColor?.setBackgroundDrawable(AirUtils.getRoundRectDrawable(AirUtils.dip2px(8f), Color.parseColor("#999999"),true,0))
            }
            holder.tvDes?.text = it.Description
        }
        holder.rlColor?.setOnClickListener {
            mOnItemClick?.setOnclickItem(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rlColor: RelativeLayout?=null
        var tvDes: TextView?=null

        init {
            rlColor = itemView.pollutionBg
            tvDes = itemView.pollution
        }
    }

    interface OnItemClickListener {
        fun setOnclickItem(position: Int)
    }

    fun setOnItemClickListener(onItemOnclick: OnItemClickListener) {
        this.mOnItemClick = onItemOnclick
    }
}