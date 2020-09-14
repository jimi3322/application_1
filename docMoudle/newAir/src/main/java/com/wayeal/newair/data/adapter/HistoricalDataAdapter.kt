package com.wayeal.newair.data.adapter

import HistoricalDatas
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wayeal.newair.R
import com.wayeal.newair.data.bean.TabelHeaderData
import kotlinx.android.synthetic.main.item_historical_data.view.*


@Suppress("DEPRECATION")
class HistoricalDataAdapter(private val mContext: Context) :
        RecyclerView.Adapter<HistoricalDataAdapter.ViewHolder>() {

    private var mDataList: MutableList<HistoricalDatas>? = null
    private var mselectDataList: MutableList<TabelHeaderData>? = null
    private var mOnItemClick: OnItemClickListener? = null
    private var mItemsAdapter: ItemsAdapter? = null
    private var mselectDatas: ArrayList<String> = ArrayList()
    private var mselectCounts: Int? = 0

    fun setData(data:ArrayList<HistoricalDatas>,selectData:ArrayList<TabelHeaderData>){
        this.mDataList = data
        this.mselectDataList = selectData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
             LayoutInflater.from(mContext).inflate(R.layout.item_historical_data, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mselectDatas.clear()
        val item = mDataList?.get(position)
        item?.let {
            mselectDatas.add(it.DataTime)
            for (selectItems in mselectDataList!!){
                if(selectItems.ComponentID == "0103-Avg"){mselectDatas.add(it.hd0103Avg)}
                if(selectItems.ComponentID == "103-Avg"){mselectDatas.add(it.hd103Avg)}
                if(selectItems.ComponentID == "107-Avg"){mselectDatas.add(it.hd107Avg)}
                if(selectItems.ComponentID == "126-Avg"){mselectDatas.add(it.hd126Avg)}
                if(selectItems.ComponentID == "127-Avg"){mselectDatas.add(it.hd127Avg)}
                if(selectItems.ComponentID == "128-Avg"){mselectDatas.add(it.hd128Avg)}
                if(selectItems.ComponentID == "129-Avg"){mselectDatas.add(it.hd129Avg)}
                if(selectItems.ComponentID == "130-Avg"){mselectDatas.add(it.hd130Avg)}
                if(selectItems.ComponentID == "911-Avg"){mselectDatas.add(it.hd911Avg)}
                if(selectItems.ComponentID == "912-Avg"){mselectDatas.add(it.hd912Avg)}
                if(selectItems.ComponentID == "925-Avg"){mselectDatas.add(it.hd925Avg)}
                if(selectItems.ComponentID == "926-Avg"){mselectDatas.add(it.hd926Avg)}
                if(selectItems.ComponentID == "927-Avg"){mselectDatas.add(it.hd927Avg)}
                if(selectItems.ComponentID == "B03-Avg"){mselectDatas.add(it.hdB03Avg)}
                if(selectItems.ComponentID == "DI1-Avg"){mselectDatas.add(it.hdDI1Avg)}
            }
            mselectCounts = mselectDatas.size
            Log.i(com.wayeal.newair.data.TAG, "mselectDatas: $mselectDatas")
            val itemManager = GridLayoutManager(mContext, mselectCounts!!)
            holder.tvRecyclerItems.layoutManager = itemManager
            mItemsAdapter = ItemsAdapter(mContext)
            holder.tvRecyclerItems.adapter = mItemsAdapter
            mItemsAdapter?.setData(mselectDatas)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRecyclerItems : RecyclerView = itemView.recyclerItems
    }

    interface OnItemClickListener {
        fun setOnclickItem(position: Int)
    }

    fun setOnItemClickListener(onItemOnclick: OnItemClickListener) {
        this.mOnItemClick = onItemOnclick
    }
}