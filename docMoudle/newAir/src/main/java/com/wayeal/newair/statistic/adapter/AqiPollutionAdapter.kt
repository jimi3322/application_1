package com.wayeal.newair.statistic.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.monitor.bean.ComponentBean
import com.wayeal.newair.statistic.bean.AQIRankingData
import com.wayeal.newair.statistic.bean.CalenderDayItem
import kotlinx.android.synthetic.main.item_aqi_pollution.view.*
import kotlinx.android.synthetic.main.item_aqi_ranking.view.*
import kotlinx.android.synthetic.main.item_calendar.view.*


class AqiPollutionAdapter(private val mContext: Context) :
        RecyclerView.Adapter<AqiPollutionAdapter.ViewHolder>() {

    private var mDataList:MutableList<ComponentBean>? = null
    private var mOnItemClick: OnItemClickListener? = null

    fun setData(data:ArrayList<ComponentBean>){
        this.mDataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_aqi_pollution, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList?.get(position)
        item?.let {
            holder.tvName.text = it.Description
            holder.tvValue.text = it.Value

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvValue: TextView = itemView.pollutanValue
        var tvName: TextView = itemView.pollutantName
    }

    interface OnItemClickListener {
        fun setOnclickItem(position: Int)
    }

    fun setOnItemClickListener(onItemOnclick: OnItemClickListener) {
        this.mOnItemClick = onItemOnclick
    }
}