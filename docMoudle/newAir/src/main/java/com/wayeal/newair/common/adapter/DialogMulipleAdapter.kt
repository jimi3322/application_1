package com.wayeal.newair.common.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.wayeal.newair.R
import com.wayeal.newair.common.bean.BaseChooseItemType
import com.wayeal.newair.common.bean.ChooseItemType
import com.wayeal.newair.data.bean.TabelHeaderData
import com.wayeal.newair.monitor.bean.ComponentBean
import com.wayeal.newair.monitor.bean.TestPointDetail
import kotlinx.android.synthetic.main.dialog_multiple_item.view.*

/**
 * 多选列表dialog适配器
 * */
class DialogMulipleAdapter(private val mContext: Context)
    : RecyclerView.Adapter<DialogMulipleAdapter.TitleViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        return TitleViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_multiple_item, parent, false)
        )
    }

    private var mDataList: ArrayList<BaseChooseItemType>? = null
    private var mOnItemClick: OnItemClickListener? = null

    fun setData(data: ArrayList<BaseChooseItemType>) {
        this.mDataList = data
        notifyDataSetChanged()
    }

    class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvDescrip :TextView = itemView.descrip
        var tvCheck :CheckBox = itemView.check
    }
    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val item = mDataList?.get(position)
//        holder.tvDescrip?.text = item?.Description
        holder.tvCheck.isChecked = item?.check!!
        when(item.returnType()){
            ChooseItemType.TableHeaderInfo -> {
                holder.tvDescrip.text = (item as TabelHeaderData).Description
            }
            ChooseItemType.station -> {
                holder.tvDescrip.text = (item as TestPointDetail).title
            }
            ChooseItemType.ComponentBean -> {
                holder.tvDescrip.text = (item as ComponentBean).Description
            }
        }

        mOnItemClick?.let {
            holder.itemView?.setOnClickListener {
                mOnItemClick?.setOnclickItem(view = holder.itemView,position = position)
            }
        }
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    interface OnItemClickListener {
        fun setOnclickItem(view: View, position: Int)
    }


    fun setOnItemClickListener(onItemOnclick: OnItemClickListener) {
        this.mOnItemClick = onItemOnclick
    }

}