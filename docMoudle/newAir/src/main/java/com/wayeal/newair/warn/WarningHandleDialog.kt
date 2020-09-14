package com.wayeal.newair.warn

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import android.widget.Toast
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.google.gson.Gson
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.common.dialog.CommonListDialog
import com.wayeal.newair.common.view.ChooseView
import com.wayeal.newair.warn.bean.WarnData
import kotlinx.android.synthetic.main.dialog_warn_handle.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class WarningHandleDialog(context: Context
                          , warnData: WarnData
                          , listener: onWarningHandleListener? = null)
    : Dialog(context, R.style.dialog_style){

    private val TAG = "WarningHandleDialog"
    private var mContext: Context? = null
    private var mWarnData: WarnData? = null
    private var mListener: onWarningHandleListener? = null

    private var mHandlerName:String? = null
    private var mHandleTime:Date? = null
    private var mHandleResult:String? = null
    private var mHandleRemark:String? = ""

    init{
        this.mContext = context
        this.mWarnData = warnData
        this.mListener = listener
        val view = View.inflate(mContext,R.layout.dialog_warn_handle,null)
        //todo 暂时这样
        view.handleName.hideArrow()
        mHandlerName = "张元浩"
        view.handleName.setValue("张元浩")
        view.handleTime.hideArrow()
        mHandleTime = Date()
        view.handleTime.setValue(AirUtils.getTimeWithFormat(Date(),"yyyy-MM-dd HH:mm:ss"))
        //处理结果
        view.handleResult.setValue("报告领导")
        mHandleResult = AirConstants.REPORT_TO_LEADER_TEXT
        val resultDialog = CommonListDialog(mContext!!
                , mContext!!.getString(R.string.handle_result)
                , AirUtils.getStringList(R.array.HandleResults)
                , object : CommonListDialog.onItemClickListener {
            override fun onResult(pos: Int, result: String) {
                view.handleResult.setValue(result)
                view.handleResult.setExpand(false)
                mHandleResult = result
            }
        })
        view.handleResult.setExpandListener(object : ChooseView.OnChooseClickListener {
            override fun onShrink() {
                resultDialog.dismiss()
            }

            override fun onExpand() {
                resultDialog.show()
            }
        })

        view.cancel.setOnClickListener {
            mListener?.onCancel()
            dismiss()
        }
        view.confirm.setOnClickListener{
            Log.i(TAG, "处理警告: ")
            mHandleRemark = view.handleTips.text.toString()
            disposalWarn()
        }
        setContentView(view)
        initAttributes()

    }

    private fun disposalWarn(){
        val disposalParams = HashMap<String, String>()
        val disposalParamList = ArrayList<HashMap<String,String>>()
        val params = HashMap<String, String>()

        disposalParams[AirConstants.DISPOSAL_ID] = UUID.randomUUID().toString()
        disposalParams[AirConstants.WARNING_ID] = mWarnData?.ID!!
        disposalParams[AirConstants.DISPOSAL_DATE] = AirUtils
                .getTimeWithFormat(mHandleTime!!,"yyyy-MM-dd HH:mm:ss")
        disposalParams[AirConstants.DISPOSAL_USER] = mHandlerName!!
        disposalParams[AirConstants.DISPOSAL_REMARK] = mHandleRemark!!

        disposalParams[AirConstants.DISPOSAL_TYPE] =
                when(mHandleResult){
            AirConstants.REPORT_TO_LEADER_TEXT -> AirConstants.REPORT_TO_LEADER
            AirConstants.NOTICE_TO_LAWER_TEXT -> AirConstants.NOTICE_TO_LAWER
            AirConstants.NOTICE_TO_OPERATION_TEXT -> AirConstants.NOTICE_TO_OPERATION
            AirConstants.NOTICE_TO_STATION_TEXT -> AirConstants.NOTICE_TO_STATION
            AirConstants.FREE_PROCESS_TEXT -> AirConstants.FREE_PROCESS
            AirConstants.ANOTHER_TEXT -> AirConstants.ANOTHER
                    else -> AirConstants.ANOTHER
        }
        disposalParamList.add(disposalParams)
        params["data"] = Gson().toJson(disposalParamList)

        OkHttpUtils.getInstance()
                .post(mContext, HttpUtil.getAirServiceAddress(AirConstants.INSERT_ALARM_DISPOSAL_INFO)
                        , params
                        , object : GsonResponseHandler<WanResponse<Object>>() {
                    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    override fun onSuccess(statusCode: Int, d: WanResponse<Object>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            Toast.makeText(mContext, "处理成功", Toast.LENGTH_SHORT).show()
                            dismiss()
                            mListener?.onConfirm()
                        }else if (statusCode == AirConstants.RESPONSE_STATUS_FAILURE){
                            Toast.makeText(mContext, "处理失败", Toast.LENGTH_SHORT).show()
                        }
                    }

                    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Toast.makeText(mContext, "处理失败", Toast.LENGTH_SHORT).show()
                    }
                })
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

    interface onWarningHandleListener{

        fun onConfirm()

        fun onCancel()

    }

}