package com.app.common.view.areaPick

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.app.common.domain.AddressBean
import com.app.wayee.common.R
import kotlinx.android.synthetic.main.item_address.view.*

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.ViewHolder>{

    private var mList: MutableList<AddressBean>?=null
    private var mOnItemClick: OnItemClickListener? = null

    interface OnItemClickListener {
        fun setOnclickItem(view: View, position: Int)
    }

    /**
     * 设置点击监听
     * @param onItemOnclick
     */
    fun setOnItemClickListener(onItemOnclick: OnItemClickListener) {
        this.mOnItemClick = onItemOnclick
    }


    constructor(list: MutableList<AddressBean>){
        this.mList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList!![position]
        if (item != null) {
            holder.tvAddress?.text =item.title
            if(item.isSelected){
                holder.ivSelected?.visibility = View.VISIBLE
                holder.tvAddress?.setTextColor(holder.tvAddress?.context?.resources?.getColor(R.color.commonBlueColor)!!)
            }else{
                holder.ivSelected?.visibility = View.GONE
                holder.tvAddress?.setTextColor(holder.tvAddress?.context?.resources?.getColor(R.color.commonTextColor)!!)
            }
            if (mOnItemClick != null) {
                holder.itemView.setOnClickListener{ v -> mOnItemClick!!.setOnclickItem(v, position) }
            }
        }

    }

    fun getList(): MutableList<AddressBean>? {
        return mList
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvAddress: TextView?=null
        var ivSelected:ImageView?=null

        init {
            tvAddress = view.addressTV
            ivSelected = view.selectedIV
        }
    }
}