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
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.monitor.bean.RealTimeData
import com.wayeal.newair.monitor.bean.RealTimeListData
import kotlinx.android.synthetic.main.item_real_time_data.view.*
import kotlinx.android.synthetic.main.item_real_time_list.view.*

@Suppress("DEPRECATION")
class RealTimeDataAdapter(private val mContext: Context) :
        RecyclerView.Adapter<RealTimeDataAdapter.ViewHolder>() {

    private var mDataList:MutableList<RealTimeData>? = null
    private var mOnItemClick: OnItemClickListener? = null

    fun setData(data:ArrayList<RealTimeData>){
        this.mDataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_real_time_data, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList?.get(position)
        item?.let {
            //todo 时间不对
            holder.tvTime?.text = it.CreateTime
            if (it.Description.isEmpty()){
                holder.tvMonitorDetail?.text = mContext.resources.getString(R.string.empty)
            }else{
                holder.tvMonitorDetail?.text = it.Description
            }
            //测量值属于非法
            val value = it.Value
           /* if (value < item.rangelow || value > item.rangehigh ||
                    value < item.abnormallow || value > item.abnormalhigh){
                holder.tvMonitorValue?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
            }*/
            holder.tvMonitorValue?.text = it.Value.toFloat().toString()
            if (it.Unit.isEmpty()){
                holder.tvUnit?.text = mContext.resources.getString(R.string.empty)
            }else{
                holder.tvUnit?.text = it.Unit
            }
            if (it.ThirdLevel.isEmpty()){
                holder.tvSecondStandard?.text = mContext.resources.getString(R.string.empty)
            }else{
                holder.tvSecondStandard?.text = it.ThirdLevel
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTime: TextView?=null
        var tvMonitorDetail: TextView?=null
        var tvMonitorValue: TextView?=null
        var tvUnit: TextView?=null
        var tvSecondStandard: TextView?=null

        init {
            tvTime = itemView.time
            tvMonitorDetail = itemView.monitorDetail
            tvMonitorValue = itemView.monitorValue
            tvUnit = itemView.unit
            tvSecondStandard = itemView.secondStandard
        }
    }

    interface OnItemClickListener {
        fun setOnclickItem(position: Int)
    }

    fun setOnItemClickListener(onItemOnclick: OnItemClickListener) {
        this.mOnItemClick = onItemOnclick
    }
}