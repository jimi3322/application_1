package com.wayeal.newair.monitor.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.monitor.bean.StandardInfo
import kotlinx.android.synthetic.main.item_legnd.view.*


class LegndAdapter(private val mContext: Context) :
        RecyclerView.Adapter<LegndAdapter.ViewHolder>() {

    private var mDataList: MutableList<StandardInfo>? = null
    private var mOnItemClick: OnItemClickListener? = null

    fun setData(data: ArrayList<StandardInfo>) {
        this.mDataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_legnd, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList?.get(position)
        item?.let {
            holder.tvRange?.text = "${item?.RangeLow}-${item?.RangeHigh}"
            if (it.StatusName.isEmpty()) {
                holder.tvDes?.text = mContext.resources.getString(R.string.no_data)
            } else {
                holder.tvDes?.text = it.StatusName
            }
            if (it.Color.isEmpty()) {
                holder.vColor?.setBackgroundDrawable(AirUtils.getRoundRectDrawable(AirUtils.dip2px(5f),AirUtils.getRgbInt(AirConstants.NO_DATA_COLOR),true,0))
//                holder.vColor?.setBackgroundColor(AirUtils.getRgbInt(AirConstants.NO_DATA_COLOR))
            } else {
                holder.vColor?.setBackgroundDrawable(AirUtils.getRoundRectDrawable(AirUtils.dip2px(5f),AirUtils.getRgbInt(it.Color),true,0))
//                holder.vColor?.setBackgroundColor(AirUtils.getRgbInt(it.Color))
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var vColor: View? = null
        var tvRange: TextView? = null
        var tvDes: TextView? = null

        init {
            vColor = itemView.color
            tvRange = itemView.range
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