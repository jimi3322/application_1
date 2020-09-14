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
import com.github.abel533.echarts.json.GsonOption
import com.github.abel533.echarts.series.Line
import com.github.abel533.echarts.series.MarkPoint
import com.github.abel533.echarts.style.TextStyle
import com.google.gson.Gson
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.statistic.base.BaseOperationFragment
import com.wayeal.newair.statistic.base.EChartView
import com.wayeal.newair.statistic.bean.ChartData
import com.wayeal.newair.statistic.bean.MarkPointData
import kotlinx.android.synthetic.main.fragment_mutli_analysis.*
import kotlinx.android.synthetic.main.fragment_mutli_analysis.chartView
import kotlinx.android.synthetic.main.fragment_single_analysis.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MutliAnalysisFragment : BaseOperationFragment() {

    private val TAG = "MutliAnalysisFragment"

    override fun getLayoutResId(): Int {
        Log.i(TAG, "getLayoutResId: ")
        return R.layout.fragment_mutli_analysis
    }

    override fun initView() {
        super.initView()
        getMutliAnalysis.setOnClickListener {
            getGraphsReportData()
        }
    }

    override fun initData() {
        super.initData()
    }

    override fun initToolbar() {
    }

    override fun returnFragmentType(): Int {
        return MUTLI
    }

    private fun getGraphsReportData(){
        if (mMutliSelectFactors.size == 0){
            Toast.makeText(mActivity, "请选择监测项目", Toast.LENGTH_SHORT).show()
            return
        }
        if (mMutliSelectStations.size == 0){
            Toast.makeText(mActivity, "请选择监测站点", Toast.LENGTH_SHORT).show()
        }
        //添加condition
        val params = HashMap<String, String>()
        val conditionParams = ArrayList<Any>()
        for (selectStation in mMutliSelectStations){
            val tmpConditionParam = HashMap<String,Any>()
            tmpConditionParam[AirConstants.ID] = selectStation.key.toString()
            tmpConditionParam[AirConstants.POLLUTION_TYPE] = "air"
            conditionParams.add(tmpConditionParam)
        }
        params[AirConstants.CONDITION] = Gson().toJson(conditionParams)
        //添加时间
        val timeParams = HashMap<String,Any>()
        timeParams[AirConstants.STARTIME] = "${AirUtils.getDate(stDate)} ${stHour}:00"
        timeParams[AirConstants.ENDTIME] = "${AirUtils.getDate(edDate)} ${edHour}:00"
        params[AirConstants.TIME] = AirUtils.mapTojson(timeParams)
        //报告类型
        params[AirConstants.REPORT_TYPE] = "multi"
        //监测项目
        val factorParams = ArrayList<String>()
        for (factor in mMutliSelectFactors){
            factorParams.add(factor.ComponentID)
        }
        params[AirConstants.COMPONENTS] = Gson().toJson(factorParams)
        OkHttpUtils.getInstance()
                .post(mActivity, HttpUtil.getAirServiceAddress(AirConstants.GET_GRAPHS_REPORT_DATA)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<ChartData>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<ChartData>>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            Log.i(TAG, "onSuccess: 获取综合趋势分析数据成功")
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
                        Log.i(TAG, "onFailure: 获取综合趋势分析数据失败")
                    }
                })
    }

    private var mLineChartDataX : ArrayList<String> = ArrayList()      //X轴
    private var mLineChartDataY : ArrayList<ArrayList<Double>> = ArrayList<ArrayList<Double>>()      //折线数据
    private var mLegend : ArrayList<String>? = ArrayList()          //图例
    private var mLegendWithUnit : String? = null            //单位

    private fun setChartData(data: ArrayList<ChartData>){
        chartView.setDataSource(object : EChartView.DataSource{
            override fun markChartOptions(): GsonOption {
                for (item in data[0].data){
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:00", Locale.getDefault())
                    val itemX = sdf.format(item.x)
                    if(item.y != null){
                        mLineChartDataX.add(itemX)
                    }
                }
                mLegendWithUnit = mMutliSelectFactors[0]
                        .Description.plus("(")
                        .plus(mMutliSelectFactors[0].Unit)
                        .plus(")")
                //获取图例信息和折线信息
                for (lineData in data){
                    mLegend?.add(lineData.key)
                    val lineDataList:ArrayList<Double> = ArrayList()
                    for (item in lineData.data){
                        if (item.y != null){
                            lineDataList.add(item.y.toDouble())
                        }
                    }
                    mLineChartDataY.add(lineDataList)

                }

                val dataArrayX = mLineChartDataX!!.toTypedArray()
                val legend = mLegend!!.toTypedArray()
                val colors = AirUtils.getStringList(R.array.Colors).toTypedArray()

                val option = GsonOption()
                option.tooltip().trigger(Trigger.axis)
                //设置图例
                option.legend(*legend)
                //设置颜色
                option.color(*colors)
                //设置字体大小
                val tooltip = Tooltip()
                val textStyle = TextStyle()
                textStyle.fontSize = 10
                tooltip.textStyle = textStyle
                option.tooltip = tooltip
                //设置grid
                val grid = Grid()
                grid.right = "15%"
                grid.left = "15%"
                grid.top = "10%"
                grid.bottom = "25%"
                option.grid = grid
                //设置x轴
                option.calculable = true
                option.xAxis(CategoryAxis()
                        .boundaryGap(false)
                        .data(*dataArrayX))
                //设置Y轴
                val yLeftAxis = ValueAxis()
                //y轴名字以及位置
                yLeftAxis.name = mLegendWithUnit
                yLeftAxis.nameLocation = NameLocation.middle
                yLeftAxis.position = "left"
                yLeftAxis.nameGap = 35
                //y轴颜色
                val axisLine = AxisLine()
                axisLine.lineStyle().color = colors[0]
                yLeftAxis.axisLine(axisLine)
                //y轴分割线 虚线
                val splitLine = SplitLine()
                splitLine.lineStyle().type = LineType.dashed
                yLeftAxis.splitLine(splitLine)

                option.yAxis(yLeftAxis)
                //添加折线数据
                for (lineData in mLineChartDataY){
                    val dataArray = lineData.toTypedArray()
                    val line = Line()
                    line.name = legend[mLineChartDataY.indexOf(lineData)]
                    line.type = SeriesType.line
                    line.data(*dataArray)
                    line.symbol = "none"
                    val markPoint = MarkPoint()
                    markPoint.data(MarkPointData("max","最大值"), MarkPointData("min","最小值"))
                    line.markPoint(markPoint)
                    option.series(line)
                }
                option.exportToHtml("line.html")
                option.view()
                return option
            }
        })
    }

}