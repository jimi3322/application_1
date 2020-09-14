package com.wayeal.newair.statistic.base

import android.util.Log
import com.github.abel533.echarts.Grid
import com.github.abel533.echarts.Tooltip
import com.github.abel533.echarts.axis.*
import com.github.abel533.echarts.code.*
import com.github.abel533.echarts.json.GsonOption
import com.github.abel533.echarts.series.Line
import com.github.abel533.echarts.series.MarkPoint
import com.github.abel533.echarts.style.TextStyle
import com.wayeal.newair.R
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.statistic.bean.ChartData
import com.wayeal.newair.statistic.bean.MarkPointData
import kotlinx.android.synthetic.main.fragment_single_analysis.chartView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseEChartFragment:BaseOperationFragment(){

    private val TAG = "BaseEChartFragment"

    override fun initView() {
        super.initView()
        initEChartView()
    }

    private fun initEChartView(){
        Log.i(TAG, "initEChartView: ")
        chartView.setDataSource(object : EChartView.DataSource{
            override fun markChartOptions(): GsonOption {

                val colors = AirUtils.getStringList(R.array.Colors).toTypedArray()

                val option = GsonOption()
                option.tooltip().trigger(Trigger.axis)
                //设置颜色
                option.color(*colors)
                //设置字体大小
                val tooltip = Tooltip()
                val textStyle = TextStyle()
                textStyle.fontSize = 30
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
                        .boundaryGap(false))
                //设置Y轴
                val yLeftAxis = ValueAxis()
                //y轴名字以及位置
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

                //设置Y轴
                val yRightAxis = ValueAxis()
                //y轴名字以及位置
                yRightAxis.nameLocation = NameLocation.middle
                yRightAxis.position = "right"
                yRightAxis.nameGap = 35
                //y轴颜色
                val axisRLine = AxisLine()
                axisRLine.lineStyle().color = colors[1]
                yRightAxis.axisLine(axisRLine)
                //y轴分割线 虚线
                val splitRLine = SplitLine()
                splitLine.lineStyle().type = LineType.dashed
                yRightAxis.splitLine(splitRLine)
                //添加2条Y轴
                option.yAxis(yLeftAxis,yRightAxis)

                option.exportToHtml("line.html")
                option.view()
                return option
            }
        })
        chartView.setType(0)
    }

    private var mLineChartDataX : ArrayList<String>? = ArrayList()      //X轴
    private var mLineChartDataLeftY : ArrayList<Double>? = ArrayList()      //左边Y轴
    private var mLineChartDataRightY : ArrayList<Double>? = ArrayList()      //右边Y轴
    private var mLegend : ArrayList<String>? = ArrayList()          //图例
    private var mLegendWithUnit : ArrayList<String> = ArrayList()

    /**
     * 设置折线图数据
     * */
    protected fun setChartData(data: ArrayList<ChartData>){
        mLineChartDataX?.clear()
        mLineChartDataLeftY?.clear()
        mLineChartDataRightY?.clear()
        mLegend?.clear()
        mLegendWithUnit.clear()
        chartView.setDataSource(object : EChartView.DataSource{
            override fun markChartOptions(): GsonOption {
                for (item in data[0].data){
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:00", Locale.getDefault())
                    val itemX = sdf.format(item.x)
                    if(item.y != null){
                        mLineChartDataX?.add(itemX)
                        mLineChartDataLeftY?.add(item.y.toDouble())
                    }
                }
                if (data.size == 2){
                    Log.i(TAG, "markChartOptions: 右边Y轴数据")
                    for (item in data[1].data){
                        if(item.y != null){
                            mLineChartDataRightY?.add(item.y.toDouble())
                        }
                    }
                }
                //获取图例信息
                for (lineData in data){
                    for (factor in mFactors){
                        if (factor.ComponentID == lineData.key){
                            Log.i(TAG, "markChartOptions: ADD = ${factor.Description}")
                            mLegend?.add(factor.Description)
                            mLegendWithUnit.add(factor.Description
                                    .plus("(")
                                    .plus(factor.Unit)
                                    .plus(")"))
                        }
                    }
                }

                val dataArrayX = mLineChartDataX!!.toTypedArray()
                val dataArrayLeftY = mLineChartDataLeftY!!.toTypedArray()
                val dataArrayRightY = mLineChartDataRightY!!.toTypedArray()
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
                textStyle.fontSize = 30
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
                yLeftAxis.name = mLegendWithUnit[0]
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

                //设置Y轴
                val yRightAxis = ValueAxis()
                //y轴名字以及位置
                yRightAxis.nameLocation = NameLocation.middle
                yRightAxis.position = "right"
                yRightAxis.nameGap = 35
                //y轴颜色
                val axisRLine = AxisLine()
                if (data.size == 2){
                    yRightAxis.name = mLegendWithUnit[1]
                    axisRLine.lineStyle().color = colors[1]
                }
                yRightAxis.axisLine(axisRLine)
                //y轴分割线 虚线
                val splitRLine = SplitLine()
                splitLine.lineStyle().type = LineType.dashed
                yRightAxis.splitLine(splitRLine)
                //添加2条Y轴
                option.yAxis(yLeftAxis,yRightAxis)
                //添加折线数据
                val lineLeft = Line()
                lineLeft.name = legend[0]
                lineLeft.type = SeriesType.line
                lineLeft.data(*dataArrayLeftY)
                lineLeft.symbol = "none"

                val markPoint = MarkPoint()
                markPoint.data(MarkPointData("max","最大值"),MarkPointData("min","最小值"))
                lineLeft.markPoint(markPoint)

                var lineRight:Line? = null
                if (data.size == 2){
                    lineRight = Line()
                    lineRight.name = legend[1]
                    lineRight.yAxisIndex(1)
                    lineRight.type = SeriesType.line
                    lineRight.data(*dataArrayRightY)
                    lineRight.symbol = "none"
                    lineRight.markPoint(MarkPoint().data(MarkPointData("max","最大值"),MarkPointData("min","最小值")))
                }
                //设置折线数据
                if (data.size == 2){
                    option.series(lineLeft,lineRight)
                }else{
                    option.series(lineLeft)
                }

                option.exportToHtml("line.html")
                option.view()
                return option
            }
        })
    }


}