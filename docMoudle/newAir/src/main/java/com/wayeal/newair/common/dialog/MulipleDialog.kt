package com.wayeal.newair.common.dialog

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.wayeal.newair.R
import com.wayeal.newair.common.adapter.DialogMulipleAdapter
import com.wayeal.newair.common.bean.BaseChooseItemType
import com.wayeal.newair.data.TAG
import com.wayeal.newair.data.bean.TabelHeaderData
import kotlinx.android.synthetic.main.dialog_list.view.dataList
import kotlinx.android.synthetic.main.dialog_list.view.title
import kotlinx.android.synthetic.main.dialog_muliple_list.view.*

class MulipleDialog(context: Context
                    , title: String
                    , list: ArrayList<TabelHeaderData>
                    , counts: ArrayList<Int>
                    , maxcounts : Int
                    , listener: onItemClickListener)
    : Dialog(context, R.style.dialog_style){

    private val TAG = "MulipleDialog"

    private var mContext: Context? = null
    private var mTitle: String? = null
    private var mDataList: ArrayList<out BaseChooseItemType>? = null
    private var mAdapter: DialogMulipleAdapter? = null
    private var mListener: onItemClickListener? = null
    private var mSelectDataList : ArrayList<BaseChooseItemType>? = ArrayList()//声明并初始化
    private var mCounts: ArrayList<Int> = ArrayList()
    private var mLastCounts: ArrayList<Int> = ArrayList()
    private var mMaxCounts: Int = 0

    init{
        this.mContext = context
        this.mTitle = title
        this.mDataList = list
        this.mMaxCounts = maxcounts
        this.mListener = listener
        this.mLastCounts = counts

        for (index in mDataList!!.indices){
            if (mDataList!![index].check){
                mCounts.add(index)
            }
        }

        val view = View.inflate(mContext,R.layout.dialog_muliple_list,null)
        view.cancelmuli.setOnClickListener {
            Log.i(com.wayeal.newair.data.TAG, "mLastCounts: "+mLastCounts)
            for (index in mDataList!!.indices){
                if (mLastCounts.contains(index)){
                    mDataList!![index].check = true
                }else{
                    mDataList!![index].check = false
                }
            }
            dismiss()
            mListener?.onCancel()
        }
        view.confirmuli.setOnClickListener {
            mLastCounts = mCounts
            mSelectDataList?.clear()
            for (bean in mDataList!!){
                Log.i(TAG, "遍历已经选择 bean check = ${bean.check} ")
                if (bean.check){
                    mSelectDataList?.add(bean)
                }
            }
            mSelectDataList?.let { mListener?.onResult(it,mCounts) }
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
        mAdapter = DialogMulipleAdapter(mContext!!)
        mAdapter?.setOnItemClickListener(object : DialogMulipleAdapter.OnItemClickListener {
            override fun setOnclickItem(view: View, position: Int) {
                //监测项限制选择个数
               if (mCounts.contains(position)){
                   mDataList!![position].check = false
                   mCounts.remove(position)
               }else{
                   if(mCounts.size < mMaxCounts){
                       mDataList!![position].check = true
                       mCounts.add(position)
                   }else{
//                     view.check.setImageResource(R.mipmap.icon_nochoose)
                       mDataList!![mCounts[0]].check = false
                       mCounts.removeAt(0)
                       mDataList!![position].check = true
                       mCounts.add(position)
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
        fun onResult(result: ArrayList<BaseChooseItemType>,mCounts:ArrayList<Int>)//pos: Int,
        fun onCancel()
    }



}