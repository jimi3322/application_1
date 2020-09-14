package com.wayeal.newair.statistic

import android.util.Log
import android.widget.Toast
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.github.abel533.echarts.Grid
import com.github.abel533.echarts.axis.CategoryAxis
import com.github.abel533.echarts.axis.ValueAxis
import com.github.abel533.echarts.code.Trigger
import com.github.abel533.echarts.data.PieData
import com.github.abel533.echarts.json.GsonOption
import com.github.abel533.echarts.series.Bar
import com.google.gson.Gson
import com.wayeal.newair.R
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.statistic.base.EChartView
import com.wayeal.newair.statistic.bean.QualityConditionData
import com.wayeal.newair.statistic.bean.SamePeriodData
import kotlinx.android.synthetic.main.fragment_contemporary_compare.*
import java.text.SimpleDateFormat

class QualityContemporaryCompareFragment : QualityConditionBaseFragment(){

    override fun getLayoutResId(): Int {
        return R.layout.fragment_contemporary_compare
    }

    override fun initView() {
        super.initView()
        getBarCompare.setOnClickListener {
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
        params["type"] = "air-samePeriod"
        OkHttpUtils.getInstance()
                .post(mActivity, HttpUtil.getAirServiceAddress(AirConstants.GENERATE_DATA_REPORT)
                        , params
                        , object : GsonResponseHandler<WanResponse<SamePeriodData>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<SamePeriodData>?) {
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            if (d?.data != null){
                                handleData(d.data)
                            }
                        }
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 获取对比图表数据失败")
                    }
                })
    }

    private var color : ArrayList<String> = ArrayList()          //颜色
    private var xData : ArrayList<String> = ArrayList()          //x轴数据
    private var datas: ArrayList<ArrayList<Int>> = ArrayList()           //树形图数据

    private fun handleData(samePeriodData: SamePeriodData){
        chartView.setDataSource(object : EChartView.DataSource{
            override fun markChartOptions(): GsonOption {
                val thisYearList:ArrayList<Int> = ArrayList()
                for (thisYearData in samePeriodData.ThisYear){
                    xData.add(thisYearData.Level)
                    thisYearList.add(thisYearData.SampleNumber)
                }
                datas.add(thisYearList)
                val lastYearList: ArrayList<Int> = ArrayList()
                for (lastYearData in samePeriodData.LastYear){
                    lastYearList.add(lastYearData.SampleNumber)
                }
                datas.add(lastYearList)

                val colors = arrayOf("#003366","#4cabce")
                val xDatas = xData.toTypedArray()
                val legends = arrayOf("今年","去年同期")

                val option = GsonOption()
                option.tooltip().trigger(Trigger.axis)
                //设置颜色
                option.color(*colors)
                //设置calculate
                option.calculable= true
                //设置grid
                val grid = Grid()
                grid.right = "15%"
                grid.left = "15%"
                grid.top = "15%"
                grid.bottom = "15%"
                option.grid = grid
                //设置图例
                option.legend(*legends)
                //设置x轴数据
                val xAxis = CategoryAxis().data(*xDatas)
                option.xAxis(xAxis)
                //设置y轴
                option.yAxis(ValueAxis())
                //设置数据
                val thisYearSeries = Bar().name("今年").data(*datas[0].toTypedArray())
                val lastYearSeries = Bar().name("去年同期").data(*datas[1].toTypedArray())
                //设置数据
                option.series(thisYearSeries,lastYearSeries)

                option.view()
                return option
            }
        })
        chartView.setType(0)
    }
}