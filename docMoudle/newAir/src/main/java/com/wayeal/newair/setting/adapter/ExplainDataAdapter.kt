package com.wayeal.newair.setting.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.wayeal.newair.R
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.setting.bean.ExplainDatas
import kotlinx.android.synthetic.main.item_aqi_explain.view.*


@Suppress("DEPRECATION")
class ExplainDataAdapter(private val mContext: Context) :
        RecyclerView.Adapter<ExplainDataAdapter.ViewHolder>() {

    private var mDataList: MutableList<ExplainDatas>? = null

    fun setData(data:ArrayList<ExplainDatas>){
        this.mDataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
             LayoutInflater.from(mContext).inflate(R.layout.item_aqi_explain, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList?.get(position)
        item?.let {
            holder.tvAQI.text = item.AQI
            holder.tvBackGroundColor.setBackgroundDrawable(AirUtils.getRoundRectDrawable(AirUtils.dip2px(5f),AirUtils.getRgbInt(item.BackGroundColor),true,0))
            holder.tvInfluenceToPerson.text = item.InfluenceToPerson
            holder.tvAdvice.text = item.Advice
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvAQI : TextView = itemView.aqirang
        var tvBackGroundColor : LinearLayout = itemView.backGroundColor
        var tvInfluenceToPerson : TextView = itemView.influenceToPerson
        var tvAdvice : TextView = itemView.advice
    }

    interface OnItemClickListener {
        fun setOnclickItem(position: Int)
    }

    fun setOnItemClickListener() {}
}