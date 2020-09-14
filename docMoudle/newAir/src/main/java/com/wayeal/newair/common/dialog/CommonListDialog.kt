package com.wayeal.newair.common.dialog

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.wayeal.newair.R
import com.wayeal.newair.common.adapter.DialogItemAdapter
import kotlinx.android.synthetic.main.dialog_list.view.*

class CommonListDialog(context: Context
                       ,title: String
                       ,list: ArrayList<String>
                       ,listener: onItemClickListener)
    : Dialog(context, R.style.dialog_style){

    private var mContext: Context? = null
    private var mTitle: String? = null
    private var mDataList: ArrayList<String>? = null
    private var mAdapter: DialogItemAdapter? = null
    private var mListener: onItemClickListener? = null

    init{
        this.mContext = context
        this.mTitle = title
        this.mDataList = list
        this.mListener = listener
        val view = View.inflate(mContext,R.layout.dialog_list,null)
        view.title.text = mTitle
        inflateRecyclerView(view)
        setContentView(view)
        initAttributes()
    }

    /**
    * 列表数据初始化
    * */
    private fun inflateRecyclerView(view: View){
        val linearLayoutManager = LinearLayoutManager(mContext,RecyclerView.VERTICAL,false)
        view.dataList.layoutManager = linearLayoutManager
        mAdapter = DialogItemAdapter(mContext!!)
        mAdapter?.setOnItemClickListener(object : DialogItemAdapter.OnItemClickListener {
            override fun setOnclickItem(view: View, position: Int) {
                mListener?.onResult(position,mDataList!![position])
                dismiss()
            }
        })
        view.dataList.adapter = mAdapter
        mAdapter?.setData(mDataList!!)
    }

    /**
     * 全屏显示dialog
     * */
    private fun initAttributes(){
        val windowManager = window?.windowManager
        val display = windowManager?.defaultDisplay
        val lp = this.window?.attributes
        lp?.height = display?.height as Int
        lp?.width = display?.width
        this.window?.attributes = lp
    }

    interface onItemClickListener{
        fun onResult(pos: Int,result: String)
    }

}