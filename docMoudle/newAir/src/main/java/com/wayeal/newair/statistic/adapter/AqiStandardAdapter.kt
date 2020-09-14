package com.wayeal.newair.statistic.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.monitor.bean.StandardInfo
import com.wayeal.newair.statistic.bean.CalenderDayItem
import kotlinx.android.synthetic.main.item_calendar.view.*
import kotlinx.android.synthetic.main.item_standard.view.*


class AqiStandardAdapter(private val mContext: Context) :
        RecyclerView.Adapter<AqiStandardAdapter.ViewHolder>() {

    private var mDataList:MutableList<StandardInfo>? = null
    private var mOnItemClick: OnItemClickListener? = null

    fun setData(data:ArrayList<StandardInfo>){
        this.mDataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_standard, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList?.get(position)
        item?.let {
            holder.tvRangeLow?.text= it.RangeLow
            holder.vColor?.setBackgroundDrawable(AirUtils.getRoundRectDrawable(AirUtils.dip2px(3f),
                    AirUtils.getRgbInt(it.Color),true,0))
//            holder.vColor?.setBackgroundColor(AirUtils.getRgbInt(it.Color))
            holder.tvDes?.text = it.StatusName
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvRangeLow: TextView?=null
        var vColor: View?=null
        var tvDes: TextView?=null

        init {
            tvRangeLow = itemView.rangeLow
            vColor = itemView.color
            tvDes = itemView.des
        }
    }

    interface OnItemClickListener {
        fun setOnclickItem(position: Int)
    }

    fun setOnItemClickListener(onItemOnclick: OnItemClickListener) {
        this.mOnItemClick = onItemOnclick
    }
}