package com.app.yun.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.yun.R
import com.app.yun.data.DeviceInfoData
import kotlinx.android.synthetic.main.item_device_info_layout.view.*

class DeviceListAdapter : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>{

    private var mList: MutableList<DeviceInfoData>?=null
    private var mOnItemClick: OnItemClickListener? = null

    interface OnItemClickListener {
        fun setOnclickItem(view: View, position: Int)
    }

    /**
     * 设置点击监听
     * @param onItemOnclick
     */
    fun setOnItemClickListener(onItemOnclick: OnItemClickListener?) {
        this.mOnItemClick = onItemOnclick
    }


    constructor(list: MutableList<DeviceInfoData>?){
        this.mList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device_info_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList!![position]
        if (item != null) {
            holder.tvTitle?.text = item.deviceName
            if (mOnItemClick != null) {
                holder.itemView.setOnClickListener{ v -> mOnItemClick!!.setOnclickItem(v, position) }
            }
        }

    }

    fun getList(): MutableList<DeviceInfoData>? {
        return mList
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

     inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle: TextView?=null
        var icon: ImageView?=null

        init {
            tvTitle = view.tvDeviceName
            icon = view.ivDevice
        }
    }
}
