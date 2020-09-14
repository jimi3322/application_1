package com.wayeal.newair.statistic

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.wayeal.newair.R
import com.wayeal.newair.common.bean.BaseChooseItemType
import com.wayeal.newair.data.bean.TabelHeaderData
import kotlinx.android.synthetic.main.dialog_list.view.dataList
import kotlinx.android.synthetic.main.dialog_list.view.title
import kotlinx.android.synthetic.main.dialog_muliple_list.view.*

class MulitiChooseDialog(context: Context
                         , title: String
                         , list: ArrayList<out BaseChooseItemType>
                         , limitCount: Int
                         , listener: onItemClickListener)
    : Dialog(context, R.style.dialog_style){

    private var mContext: Context? = null
    private var mTitle: String? = null
    private var mDataList: ArrayList<out BaseChooseItemType>? = null
    private var mAdapter: DialogMulitiChooseAdapter? = null
    private var mListener: onItemClickListener? = null
    private var mLimitCount: Int = 0
    private var mSelectDataList : ArrayList<BaseChooseItemType>? = ArrayList()//声明并初始化
    private var mCounts: ArrayList<Int> = ArrayList()

    init{
        this.mContext = context
        this.mTitle = title
        this.mDataList = list
        this.mListener = listener
        this.mLimitCount = limitCount

        for (index in mDataList!!.indices){
            if (mDataList!![index].check){
                mCounts.add(index)
            }
        }

        val view = View.inflate(mContext,R.layout.dialog_muliple_list,null)
        view.cancelmuli.setOnClickListener {
            dismiss()
        }
        view.confirmuli.setOnClickListener {
            mSelectDataList?.clear()
            for (bean in mDataList!!){
                Log.i(TAG, "遍历已经选择 bean check = ${bean.check} ")
                if (bean.check){
                    mSelectDataList?.add(bean)
                }
            }
            Log.i(TAG, "confirm ")
            mSelectDataList?.let { mListener?.onResult(it) }
            dismiss()
        }
        view.title.text = mTitle
        inflateRecyclerView(view)
        setContentView(view)
        initAttributes()
    }

    /**
    * 多选列表数据初始化
    * */
    private fun inflateRecyclerView(view: View){
        val linearLayoutManager = LinearLayoutManager(mContext,RecyclerView.VERTICAL,false)
        view.dataList.layoutManager = linearLayoutManager
        mAdapter = DialogMulitiChooseAdapter(mContext!!)
        mAdapter?.setOnItemClickListener(object : DialogMulitiChooseAdapter.OnItemClickListener {
            override fun setOnclickItem(view: View, position: Int) {
                if (mCounts.contains(position)){
                    Log.i(TAG, "setOnclickItem: 取消选择")
                    mCounts.remove(position)
                    mDataList!![position].check = false
                }else{
                    Log.i(TAG, "setOnclickItem: 进行选择")
                    if (mCounts.size < mLimitCount){
                        mCounts.add(position)
                        mDataList!![position].check = true
                    }else{
                        mDataList!![mCounts[0]].check = false
                        mCounts.removeAt(0)
                        mCounts.add(position)
                        mDataList!![position].check = true
                    }
                }
                mAdapter?.setData(mDataList!! as ArrayList<BaseChooseItemType>)
            }
        })
        view.dataList.adapter = mAdapter
        mAdapter?.setData(mDataList!! as ArrayList<BaseChooseItemType>)
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
        fun onResult(result: ArrayList<BaseChooseItemType>)
    }



}