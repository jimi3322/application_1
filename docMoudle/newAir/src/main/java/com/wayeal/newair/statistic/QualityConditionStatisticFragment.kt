package com.wayeal.newair.statistic

import android.util.Log
import android.widget.Toast
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.github.abel533.echarts.Grid
import com.github.abel533.echarts.Tooltip
import com.github.abel533.echarts.axis.AxisLine
import com.github.abel533.echarts.axis.CategoryAxis
import com.github.abel533.echarts.axis.SplitLine
import com.github.abel533.echarts.axis.ValueAxis
import com.github.abel533.echarts.code.LineType
import com.github.abel533.echarts.code.NameLocation
import com.github.abel533.echarts.code.SeriesType
import com.github.abel533.echarts.code.Trigger
import com.github.abel533.echarts.data.PieData
import com.github.abel533.echarts.json.GsonOption
import com.github.abel533.echarts.series.Line
import com.github.abel533.echarts.series.MarkPoint
import com.github.abel533.echarts.series.Pie
import com.github.abel533.echarts.style.TextStyle
import com.google.gson.Gson
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.statistic.base.EChartView
import com.wayeal.newair.statistic.bean.ChartData
import com.wayeal.newair.statistic.bean.MarkPointData
import com.wayeal.newair.statistic.bean.QualityConditionData
import com.wayeal.newair.statistic.bean.QualityProportion
import kotlinx.android.synthetic.main.fragment_quality_condition_statistic.*
import kotlinx.android.synthetic.main.fragment_quality_condition_statistic.chartView
import kotlinx.android.synthetic.main.fragment_single_analysis.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class QualityConditionStatisticFragment :QualityConditionBaseFragment(){

    override fun getLayoutResId(): Int {
        return R.layout.fragment_quality_condition_statistic
    }

    override fun initView() {
        super.initView()
        getPieQualityStatistic.setOnClickListener {
            getDataReport()
        }
    }

    override fun initData() {
        super.initData()
    }

    override fun initToolbar() {
    }

    private fun getDataReport(){
        if (mSelectStation == null){
            Toast.makeText(mActivity, "请选择站点", Toast.LENGTH_SHORT).show()
            return
        }
        val params = HashMap<String, String>()
        val conditionParams = ArrayList<Any>()
        conditionParams.add("${mSelectProvince?.key}/${mSelectCity?.key}/${mSelectCounty?.key}/${mSelectStation?.key}")
        params[AirConstants.CONDITION] = Gson().toJson(conditionParams)
        val timeParams = HashMap<String,Any>()
        val fot = SimpleDateFormat("yyyy/MM/dd")
        timeParams[AirConstants.STARTIME] = "${fot.format(stDate)} 00:00:00"
        timeParams[AirConstants.ENDTIME] = "${fot.format(edDate)} 23:59:59"
        params[AirConstants.TIME] = AirUtils.mapTojson(timeParams)
        params["type"] = "air-qualityStatus"
        val factorParams = ArrayList<HashMap<String,Any>>()
        for (elem in checkElems){
            val tmpConditionParam = HashMap<String,Any>()
            tmpConditionParam[AirConstants.COMPONENT_ID] = elem
            tmpConditionParam["Type"] = "air-qualityStatus"
            factorParams.add(tmpConditionParam)
        }
        params[AirConstants.COMPONENTS] = Gson().toJson(factorParams)
        OkHttpUtils.getInstance()
                .post(mActivity, HttpUtil.getAirServiceAddress(AirConstants.GENERATE_DATA_REPORT)
                        , params
                        , object : GsonResponseHandler<WanResponse<QualityConditionData>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<QualityConditionData>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            if (d?.data != null){
                                handleData(d.data.qualityProportion)
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 获取对比图表数据失败")
                    }
                })
    }

    private var color : ArrayList<String> = ArrayList()          //颜色
    private var legand : ArrayList<String> = ArrayList()          //图例
    private var pies: ArrayList<PieData> = ArrayList()           //Pie数据

    private fun handleData(data: ArrayList<QualityProportion>){
        color.clear()
        legand.clear()
        pies.clear()
        chartView.setDataSource(object : EChartView.DataSource{
            override fun markChartOptions(): GsonOption {
                for (pieData in data){
                    when(pieData.item){
                        AirConstants.EXCELLENT_TEXT -> pieData.color = AirConstants.EXCELLENT
                        AirConstants.GOOD_TEXT -> pieData.color = AirConstants.GOOD
                        AirConstants.SLIGHTLY_POLLUTED_TEXT -> pieData.color = AirConstants.SLIGHTLY_POLLUTED
                        AirConstants.MIDDLE_POLLUTED_TEXT -> pieData.color = AirConstants.MIDDLE_POLLUTED
                        AirConstants.HEAVY_POLLUTED_TEXT -> pieData.color = AirConstants.HEAVY_POLLUTED
                        AirConstants.SERVE_POLLUTED_TEXT -> pieData.color = AirConstants.SERVE_POLLUTED
                    }
                    color.add(pieData.color)
                    legand.add(pieData.item)
                    pies.add(PieData(pieData.item,pieData.percent))
                }

                val colors = color.toTypedArray()
                val legands = legand.toTypedArray()
                val pieDatas = pies.toTypedArray()

                val option = GsonOption()
                option.tooltip().trigger(Trigger.item)
                //设置grid
                val grid = Grid()
                grid.right = "15%"
                grid.left = "15%"
                grid.top = "15%"
                grid.bottom = "15%"
                option.grid = grid
                //设置颜色
                option.color(*colors)
                //设置图例
                option.legend(*legands)
                //设置数据
                val pie = Pie().name("质量状况统计").data(*pieDatas).center("50%", "60%").radius("55%")
                option.series(pie)

//                option.exportToHtml("line.html")
                option.view()
                return option
            }
        })
        chartView.setType(0)
    }
}