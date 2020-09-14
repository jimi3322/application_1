package com.wayeal.newair.warn.adapter

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.warn.bean.WarnData
import kotlinx.android.synthetic.main.item_warn.view.*


class WarnAdapter(private val mContext: Context) :
        RecyclerView.Adapter<WarnAdapter.ViewHolder>() {

    private var mDataList:MutableList<WarnData>? = null
    private var mOnItemClick: OnWarnItemClickListener? = null

    fun setData(data:ArrayList<WarnData>){
        this.mDataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_warn, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDataList?.get(position)
        item?.let {
            holder.tvName?.text = it.Name
            holder.tvTime?.text = it.StartDate
            if (it.Type.isNullOrEmpty()){
                holder.tvType?.text = AirConstants.OFFLINE_TEXT
            }else{
                holder.tvType?.text = when(it.Type){
                    AirConstants.OVERPROOF_WARNING ->  AirConstants.OVERPROOF_WARNING_TEXT
                    AirConstants.ABNORMAL_WARNING ->  AirConstants.ABNORMAL_WARNING_TEXT
                    else ->  AirConstants.OFFLINE_WARNING_TEXT
                }
            }

            if (it.DisposalState){
                holder.tvHandle?.setTextColor(mContext.resources.getColor(R.color.blackText))
                holder.tvHandle?.isEnabled = false
            }else{
                holder.tvHandle?.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                holder.tvHandle?.setOnClickListener {
                    mOnItemClick?.onClickHandle(position)
                }
            }
        }
        holder.tvDetail?.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        holder.tvDetail?.setOnClickListener {
            mOnItemClick?.onClickDetail(position)
        }



    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView?=null
        var tvTime: TextView?=null
        var tvType: TextView?=null
        var tvDetail: TextView? = null
        var tvHandle: TextView? = null

        init {
            tvName = itemView.station
            tvTime = itemView.time
            tvType = itemView.type
            tvDetail = itemView.detail
            tvHandle = itemView.handle
        }
    }

    interface OnWarnItemClickListener {
        fun onClickDetail(position: Int)

        fun onClickHandle(position: Int)
    }

    fun setOnItemClickListener(onItemOnclick: OnWarnItemClickListener) {
        this.mOnItemClick = onItemOnclick
    }
}