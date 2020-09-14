package com.wayeal.newair.statistic.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.statistic.bean.AQICalendarDayInfo
import com.wayeal.newair.statistic.bean.CalenderDayItem
import kotlinx.android.synthetic.main.item_calendar.view.*


class AqiCalendarAdapter(private val mContext: Context) :
        RecyclerView.Adapter<AqiCalendarAdapter.ViewHolder>() {

    private var mDataList:MutableList<CalenderDayItem>? = null
    private var mOnItemClick: OnItemClickListener? = null
    private var mSelectDay: String = "0"
    private var mSelectDayInfo:AQICalendarDayInfo? = null

    fun setData(data:ArrayList<CalenderDayItem>){
        this.mDataList = data
        notifyDataSetChanged()
    }

    fun getSelectDayInfo(): AQICalendarDayInfo? {
        return mSelectDayInfo
    }

    fun setSelectDay(selectDay: String){
        this.mSelectDay = selectDay
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_calendar, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList?.get(position)
//        Log.i("test", "onBindViewHolder: test position = $position ${item?.dayInfo?.time}  ${item?.isShow}  ${item?.dayInfo?.color}  " +
//                "${item?.dayInfo?.name}  ${item?.dayInfo?.value}  ${item?.dayInfo?.PrimaryPollutant} ${item?.dayInfo?.componentList}")
        item?.let {
            if (item.isShow){
//                Log.i("test", "onBindViewHolder: show 此时pos = $position 设置tvdate = ${item.dayInfo?.time}")
                val dayInfo = item.dayInfo
                holder.tvDate.text = dayInfo?.time
                //time等于selectDay就是选中效果
                if (mSelectDay == dayInfo?.time){
                    Log.i("test", "onBindViewHolder: 选中了")
                    holder.vSelectColor?.setBackgroundDrawable(AirUtils.getRoundRectDrawable(AirUtils.dip2px(5f)
                            ,mContext.getColor(R.color.white)
                            ,true
                            ,0))
                    mSelectDayInfo = mDataList?.get(position)?.dayInfo
                }else{
                    holder.vSelectColor?.setBackgroundDrawable(AirUtils.getRoundRectDrawable(AirUtils.dip2px(5f)
                            ,mContext.getColor(R.color.transparent)
                            ,true
                            ,0))
                }
                if (dayInfo?.PrimaryPollutant.isNullOrEmpty()){
                    holder.tvPollution.text = mContext.resources.getString(R.string.empty)
                }else{
                    holder.tvPollution.text = dayInfo?.PrimaryPollutant
                }
                if (dayInfo?.value == null || dayInfo.componentList == null){
                    holder.vColor.setBackgroundDrawable(AirUtils.getRoundRectDrawable(AirUtils.dip2px(5f)
                            ,AirUtils.getRgbInt(AirConstants.NO_DATA_COLOR)
                            ,true
                            ,0))
                }else{
                    holder.vColor.setBackgroundDrawable(AirUtils.getRoundRectDrawable(AirUtils.dip2px(5f)
                            ,AirUtils.getRgbInt(dayInfo.color)
                            ,true
                            ,0))

                }
                holder.itemView.setOnClickListener {
                    mOnItemClick?.setOnclickItem(position)
                }
            }else{
//                holder.vColor.setBackgroundColor(mContext.getColor(R.color.transparent))
//                holder.tvDate.visibility = View.GONE
//                holder.tvPollution.visibility = View.GONE
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vColor: LinearLayout=itemView.bgColor
        var tvDate: TextView=itemView.date
        var tvPollution: TextView=itemView.pollution
        var vSelectColor: RelativeLayout? = itemView.selectBgColor


    }

    interface OnItemClickListener {
        fun setOnclickItem(position: Int)
    }

    fun setOnItemClickListener(onItemOnclick: OnItemClickListener) {
        this.mOnItemClick = onItemOnclick
    }
}