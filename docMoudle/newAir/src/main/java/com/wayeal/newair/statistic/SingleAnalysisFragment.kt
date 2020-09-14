package com.wayeal.newair.statistic

import android.util.Log
import android.widget.Toast
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.github.abel533.echarts.json.GsonOption
import com.google.gson.Gson
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.statistic.base.BaseBusinessFragment
import com.wayeal.newair.statistic.base.BaseEChartFragment
import com.wayeal.newair.statistic.bean.ChartData
import kotlinx.android.synthetic.main.fragment_single_analysis.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SingleAnalysisFragment : BaseEChartFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fragment_single_analysis
    }

    override fun initView() {
        super.initView()
        getSingleAnalysis.setOnClickListener {
            getGraphsReportData()
        }
    }

    override fun initData() {
        super.initData()
    }

    override fun returnFragmentType(): Int {
        return SINGLE
    }


    /**
     * 获取表格信息
     * */
    private fun getGraphsReportData(){
        if (mSingleSelectFactors.size == 0){
            Toast.makeText(mActivity, "请选择站点", Toast.LENGTH_SHORT).show()
            return
        }
        val params = HashMap<String, String>()
        val conditionParams = ArrayList<Any>()
        val tmpConditionParam = HashMap<String,Any>()
        tmpConditionParam[AirConstants.ID] = mSelectStation?.key.toString()
        tmpConditionParam[AirConstants.POLLUTION_TYPE] = "air"
        conditionParams.add(tmpConditionParam)
        params[AirConstants.CONDITION] = Gson().toJson(conditionParams)
        val timeParams = HashMap<String,Any>()
        timeParams[AirConstants.STARTIME] = "${AirUtils.getDate(stDate)} ${stHour}:00"
        timeParams[AirConstants.ENDTIME] = "${AirUtils.getDate(edDate)} ${edHour}:00"
        params[AirConstants.TIME] = AirUtils.mapTojson(timeParams)
        params[AirConstants.REPORT_TYPE] = "single"
        val factorParams = ArrayList<String>()
        for (factor in mSingleSelectFactors){
            factorParams.add(factor.ComponentID)
        }
        params[AirConstants.COMPONENTS] = Gson().toJson(factorParams)
        OkHttpUtils.getInstance()
                .post(mActivity, HttpUtil.getAirServiceAddress(AirConstants.GET_GRAPHS_REPORT_DATA)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<ChartData>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<ChartData>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            Log.i(TAG, "onSuccess: 获取对比图标数据成功")
                            if (d?.data != null){
                                if (d.data.size > 0){
                                    Log.i(TAG, "onSuccess: 渲染图表")
                                    setChartData(d.data)
                                    chartView.setType(0)
                                }

                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 获取对比图表数据失败")
                    }
                })
    }
}