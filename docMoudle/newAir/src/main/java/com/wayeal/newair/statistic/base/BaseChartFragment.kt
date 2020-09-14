//package com.wayeal.newair.statistic.base
//
//import android.util.Log
//import com.github.mikephil.charting.components.XAxis
//import com.github.mikephil.charting.components.YAxis
//import com.github.mikephil.charting.data.Entry
//import com.github.mikephil.charting.data.LineData
//import com.github.mikephil.charting.data.LineDataSet
//import com.wayeal.newair.R
//import com.wayeal.newair.statistic.bean.ChartData
//import kotlinx.android.synthetic.main.fragment_single_analysis.*
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.collections.ArrayList
//
///**
// * 为了极致封装，这里对ChartView单独做处理，
// * 因为后面不确定是使用MPChart还是EChart
// * */
//const val TAG = "BaseChartFragment"
//abstract class BaseChartFragment : BaseOperationFragment(){
//
//    private val entryBlueList = ArrayList<Entry>()
//    private val yBlueList = ArrayList<Float>()
//    private val entryRedList = ArrayList<Entry>()
//    private val yRedList = ArrayList<Float>()
//    var lineDataSetBlue: LineDataSet? = null
//    var lineDataSetRed: LineDataSet? = null
//    var data:LineData? = null               //设置给LineChart的data
//
////    override fun initView() {
//        super.initView()
//        initChartView()
//    }
//
//    private fun initChartView(){
//        chart.setDrawBorders(false)
//        chart.isDragEnabled = true
//        chart.description = null
//        chart.setDrawGridBackground(false)
//        chart.isHorizontalScrollBarEnabled = true
//        chart.isVerticalScrollBarEnabled = true
//        chart.setScaleEnabled(true)
//        chart.setPinchZoom(false)
//        chart.setNoDataText("暂无数据,请点击查询按钮")
//
//
//        val xAxis: XAxis = chart.xAxis
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.setDrawGridLines(false)
//        xAxis.granularity = 1f
//        xAxis.setLabelCount(2, false)
////        //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
////        Log.i(TAG, "handleChartData: xMin = ${points[0].x}")
////        Log.i(TAG, "handleChartData: xMax = ${points.last().x}")
//        //设置最大值和最小值
//
//        xAxis.setAvoidFirstLastClipping(false)
//        xAxis.setDrawGridLines(false)
//        xAxis.setValueFormatter { value, _ ->
//            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
//            sdf.format(value)
//        }
//        xAxis.axisLineColor = mActivity.resources.getColor(R.color.bg_black)
//        xAxis.axisLineWidth = 2f
//
//        val yAxis: YAxis = chart.axisLeft
//        val rightYAxis: YAxis = chart.axisRight
//        rightYAxis.isEnabled = true
//        yAxis.setDrawGridLines(true)
//        rightYAxis.setDrawGridLines(true)
//        yAxis.enableGridDashedLine(10f,10f,0f)
//        rightYAxis.enableGridDashedLine(10f,10f,0f)
//        yAxis.setLabelCount(6, false)
//        rightYAxis.setLabelCount(6, false)
//
//        yAxis.axisLineColor = mActivity.resources.getColor(R.color.commonBlueColor)
//        rightYAxis.axisLineColor = mActivity.resources.getColor(R.color.red)
//        yAxis.axisLineWidth = 2f
//        rightYAxis.axisLineWidth = 2f
//        yAxis.textColor = mActivity.resources.getColor(R.color.commonBlueColor)
//        rightYAxis.textColor = mActivity.resources.getColor(R.color.red)
////        chart.data = data
//        chart.invalidate()
//    }
//
//    protected fun setChart(dataList: ArrayList<ChartData>){
//        chart.clear()
//        lineDataSetBlue?.clear()
//        yBlueList.clear()
//        lineDataSetRed?.clear()
//        yRedList.clear()
//        data = null
//        Log.i(TAG, "setChart: ")
//        for (index in dataList.indices){
//            if (index == 0){
//                for (point in dataList[index].data){
//                    point.y?.toFloat()?.let { Entry(point.x.toFloat(), it) }?.let { entryBlueList.add(it) }
//                    point.y?.toFloat()?.let { yBlueList.add(it) }
//                }
//                lineDataSetBlue = LineDataSet(entryBlueList,dataList[index].key)
//                lineDataSetBlue?.color = mActivity.resources.getColor(R.color.commonBlueColor)
//                lineDataSetBlue?.lineWidth = 3f
//                lineDataSetBlue?.setDrawCircles(false)
//                lineDataSetBlue?.mode = LineDataSet.Mode.CUBIC_BEZIER
//            }else if (index == 1){
//                for (point in dataList[index].data){
//                    point.y?.toFloat()?.let { Entry(point.x, it) }?.let { entryRedList.add(it) }
//                    point.y?.toFloat()?.let { yRedList.add(it) }
//                }
//                lineDataSetRed = LineDataSet(entryRedList,dataList[index].key)
//                lineDataSetRed?.color = mActivity.resources.getColor(R.color.red)
//                lineDataSetRed?.lineWidth = 3f
//                lineDataSetRed?.setDrawCircles(false)
//                lineDataSetRed?.mode = LineDataSet.Mode.CUBIC_BEZIER
//                lineDataSetRed?.axisDependency = YAxis.AxisDependency.RIGHT
//            }
//        }
//        setXAxis()
//        setYAxis()
//        data = if (lineDataSetRed != null){
//            LineData(lineDataSetBlue,lineDataSetRed)
//        }else{
//            LineData(lineDataSetBlue)
//        }
//        data!!.setDrawValues(false)
//        chart.data = data
//        chart.invalidate()
//    }
//
//    /**
//     * 设置X轴数据
//     * */
//    private fun setXAxis(){
//        val xAxis: XAxis = chart.xAxis
//        xAxis.axisMinimum = entryBlueList[0].x
//        xAxis.axisMaximum = entryBlueList.last().x
//    }
//
//    /**
//     * 设置Y轴数据
//     * */
//    private fun setYAxis(){
//        val yAxis: YAxis = chart.axisLeft
//        val rightYAxis: YAxis = chart.axisRight
//        //设置左Y轴值
//        yAxis.axisMinimum = 0f
//        Log.i(TAG, "setYAxis: leftY = ${yBlueList.max()}")
//        yAxis.axisMaximum = yBlueList.max()!! + 10
//        //设置右Y轴值
//        if (yRedList.size > 1){
//            rightYAxis.axisMinimum = 0f
//            Log.i(TAG, "setYAxis: rightY = ${yRedList.max()}")
//            rightYAxis.axisMaximum = yRedList.max()!! + 10
//        }
//    }
//}