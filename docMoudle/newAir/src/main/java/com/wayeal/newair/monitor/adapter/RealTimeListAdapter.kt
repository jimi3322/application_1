package com.wayeal.newair.monitor.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.monitor.bean.RealTimeListData
import kotlinx.android.synthetic.main.item_real_time_list.view.*

@Suppress("DEPRECATION")
class RealTimeListAdapter(private val mContext: Context) : RecyclerView.Adapter<RealTimeListAdapter.ViewHolder>() {

    private var mDataList:MutableList<RealTimeListData>? = null
    private var mOnItemClick: OnItemClickListener? = null

    fun setData(data:ArrayList<RealTimeListData>){
        this.mDataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_real_time_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList?.get(position)
        item?.let {
            if (item.Name.isEmpty()){
                holder.tvArea?.text = mContext.resources.getString(R.string.empty)
            }else{
                holder.tvArea?.text = item.Name
            }
            if (item.AQI.toString().isEmpty() || item.AQI == 0){
                holder.tvAQIValue?.text = mContext.resources.getString(R.string.empty)
            }else{
                holder.tvAQIValue?.text = item.AQI.toString()
            }
            holder.rlAQIColor?.setBackgroundDrawable(AirUtils.getRoundRectDrawable(AirUtils.dip2px(5f)
                    ,AirUtils.getRgbInt(item.Color)
                    ,true
                    ,0))
            if (item.PrimaryPollutant.isNullOrEmpty()){
                holder.tvPollutant?.text = mContext.resources.getString(R.string.empty)
            }else{
                holder.tvPollutant?.text = item.PrimaryPollutant
            }
            when(item.Status){
                AirConstants.ONLINE -> {
                    holder.tvSessionState?.text = mContext.resources.getString(R.string.online)
                    holder.tvSessionState?.setTextColor(Color.parseColor("#008000"))
                }
                AirConstants.UPLINE -> {
                    holder.tvSessionState?.text = mContext.resources.getString(R.string.upline)
                    holder.tvSessionState?.setTextColor(Color.parseColor("#000000"))
                }
                AirConstants.OFFLINE -> {
                    holder.tvSessionState?.text = mContext.resources.getString(R.string.offline)
                    holder.tvSessionState?.setTextColor(Color.parseColor("#808080"))
                }
                else -> {
                    holder.tvSessionState?.text = mContext.resources.getString(R.string.empty)
                }
            }
            when(item.SystemStatus){
                AirConstants.NORMAL -> {
                    holder.tvSystemState?.text = AirConstants.NORMAL_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_normal))
                }
                AirConstants.SUPER_UPPER_LIMIT -> {
                    holder.tvSystemState?.text = AirConstants.SUPER_UPPER_LIMIT_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
                AirConstants.SUPER_LOWER_LIMIT -> {
                    holder.tvSystemState?.text = AirConstants.SUPER_LOWER_LIMIT_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
                AirConstants.INSTRUMENT_FAILURE -> {
                    holder.tvSystemState?.text = AirConstants.INSTRUMENT_FAILURE_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
                AirConstants.INSTRUMENT_COMMUNICATION_FAILURE -> {
                    holder.tvSystemState?.text = AirConstants.INSTRUMENT_COMMUNICATION_FAILURE_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
                AirConstants.INSTRUMENT_OFFLINE -> {
                    holder.tvSystemState?.text = AirConstants.INSTRUMENT_OFFLINE_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
                AirConstants.MAINTAIN_DEBUG_DATA -> {
                    holder.tvSystemState?.text = AirConstants.MAINTAIN_DEBUG_DATA_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
                AirConstants.LACK_OF_REAGENTS -> {
                    holder.tvSystemState?.text = AirConstants.LACK_OF_REAGENTS_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
                AirConstants.LACK_OF_WATER -> {
                    holder.tvSystemState?.text = AirConstants.LACK_OF_WATER_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
                AirConstants.WATER_SHORTAGE -> {
                    holder.tvSystemState?.text = AirConstants.WATER_SHORTAGE_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
                AirConstants.LACK_OF_STANDARD_SAMPLE -> {
                    holder.tvSystemState?.text = AirConstants.LACK_OF_STANDARD_SAMPLE_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
                AirConstants.STANDARD_RECOVERY -> {
                    holder.tvSystemState?.text = AirConstants.STANDARD_RECOVERY_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
                AirConstants.PARALLEL_SAMPLE_TEST -> {
                    holder.tvSystemState?.text = AirConstants.PARALLEL_SAMPLE_TEST_TEXT
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
                else -> {
                    holder.tvSystemState?.text = mContext.resources.getString(R.string.empty)
                    holder.tvSystemState?.setTextColor(mContext.resources.getColor(R.color.system_state_unnormal))
                }
            }
            holder.tvHandle?.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            holder.tvHandle?.setOnClickListener {
                mOnItemClick?.setOnclickItem(position)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvArea: TextView?=null
        var tvAQIValue: TextView?=null
        var rlAQIColor: RelativeLayout?=null
        var tvPollutant: TextView?=null
        var tvSessionState: TextView?=null
        var tvSystemState: TextView?=null
        var tvHandle: TextView?=null

        init {
            tvArea = itemView.station
            tvAQIValue = itemView.aqi
            rlAQIColor = itemView.aqiBg
            tvPollutant = itemView.pollutant
            tvSessionState = itemView.sessionState
            tvSystemState = itemView.systemState
            tvHandle = itemView.handle
        }
    }

    interface OnItemClickListener {
        fun setOnclickItem(position: Int)
    }

    fun setOnItemClickListener(onItemOnclick: OnItemClickListener) {
        this.mOnItemClick = onItemOnclick
    }
}