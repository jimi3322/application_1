package com.wayeal.newair.warn

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.adapter.DialogMulipleAdapter
import com.wayeal.newair.data.bean.TabelHeaderData
import com.wayeal.newair.warn.bean.WarnData
import kotlinx.android.synthetic.main.dialog_list.view.dataList
import kotlinx.android.synthetic.main.dialog_list.view.title
import kotlinx.android.synthetic.main.dialog_muliple_list.view.*
import kotlinx.android.synthetic.main.dialog_multiple_item.view.*
import kotlinx.android.synthetic.main.dialog_warn_detail.view.*
import java.lang.reflect.Array

class WarningDetailDialog(context: Context
                          , warnData: WarnData)
    : Dialog(context, R.style.dialog_style){

    private var mContext: Context? = null
    private var mWarnData: WarnData? = null

    init{
        this.mContext = context
        this.mWarnData = warnData

        val view = View.inflate(mContext,R.layout.dialog_warn_detail,null)
        view.confirm.setOnClickListener {
            dismiss()
        }
        view.name.text = mWarnData?.Name
        view.time.text = mWarnData?.Date
        if (mWarnData?.Type.isNullOrEmpty()){
            view.warnType?.text = AirConstants.OFFLINE_TEXT
        }else{
            view.warnType?.text = when(mWarnData?.Type){
                AirConstants.OVERPROOF_WARNING ->  AirConstants.OVERPROOF_WARNING_TEXT
                AirConstants.ABNORMAL_WARNING ->  AirConstants.ABNORMAL_WARNING_TEXT
                else ->  AirConstants.OFFLINE_WARNING_TEXT
            }
        }
        if (mWarnData?.DisposalState != null){
            if (mWarnData?.DisposalState!!){
                view.warnStatus.text = "已处理"
            }else{
                view.warnStatus.text = "未处理"
            }
        }

        view.warnTime.text = "${mWarnData?.StartDate}-${mWarnData?.EndDate}共持续了${mWarnData?.Duration}分钟"
        setContentView(view)
        initAttributes()
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

}