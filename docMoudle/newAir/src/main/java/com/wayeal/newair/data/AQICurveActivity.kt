package com.wayeal.newair.data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import com.app.baselib.http.OkHttpUtils
import com.app.baselib.http.response.GsonResponseHandler
import com.app.common.utils.HttpUtil
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.github.abel533.echarts.axis.CategoryAxis
import com.github.abel533.echarts.axis.ValueAxis
import com.github.abel533.echarts.code.Trigger
import com.github.abel533.echarts.json.GsonOption
import com.github.abel533.echarts.series.Line
import com.github.abel533.echarts.series.MarkLine
import com.github.abel533.echarts.series.MarkPoint
import com.wayeal.newair.R
import com.wayeal.newair.base.BaseActivity
import com.wayeal.newair.common.AirConstants
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.bean.WanResponse
import com.wayeal.newair.common.dialog.ChoosePointDialog
import com.wayeal.newair.common.view.ChooseView
import com.wayeal.newair.data.bean.*
import com.wayeal.newair.monitor.bean.*
import com.wayeal.newair.statistic.bean.MarkPointData
import kotlinx.android.synthetic.main.activity_aqi_curve.*
import kotlinx.android.synthetic.main.activity_historical_data.getDatas
import kotlinx.android.synthetic.main.activity_historical_data.historyPoint
import kotlinx.android.synthetic.main.activity_historical_data_curve.chartView
import kotlinx.android.synthetic.main.activity_historical_data_curve.end_date
import kotlinx.android.synthetic.main.activity_historical_data_curve.start_date
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.common_blue_tablayout.view.title
import kotlinx.android.synthetic.main.common_item_with_arrow.view.*
import kotlinx.android.synthetic.main.fragment_statistic.toolbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@Suppress("NAME_SHADOWING")
class AQICurveActivity : BaseActivity() , EChartWebView.DataSource{

    private val mContext: Context? = this
    private var mSelectStation: TestPointDetail? = null       //选择的监测站点
    private var mSelectCounty: CountyPoint? = null      //选择的区
    private var mSelectCity: CityPoint? = null          //选择的城市
    private var mSelectProvince: TestPoint? = null      //选择的省
    private var mProvinces:ArrayList<TestPoint> = ArrayList()        //获取的站点信息
    private var mAQIDataCurve : AQIDataPoint? = null        //AQI数据
    private var rangehigh : Int = 0                         //上限值
    private var rangelow : Int = 0                         //下限值

    private var startDate:String = ""
    private var endDate:String = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_aqi_curve
    }

    override fun initView() {
        initDateTime()
        initPoint()
        initItemView()
    }

    /**
     * 在WebView中加载网页
     */
    fun loadPage(url: String?) {
        if (url == null) return
        initWebview(url)
    }

    /**
     * WebView组件添加WebViewClient
     */
    private fun initWebview(url: String) {
        chartView!!.loadUrl(url)
        chartView!!.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                Log.i(TAG, "onPageStarted............................")
                loading.showLoading()
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                Log.i(TAG, "onPageFinished..............................")
                loading.showContent()
                super.onPageFinished(view, url)
            }
        }
    }

    /**
     * 选择Item的View
     **/
    private fun initItemView(){
        //开始时间
        start_date.value.text = startDate
        start_date.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                TimePickerBuilder(this@AQICurveActivity, OnTimeSelectListener {
                    date, _ ->
                    Log.i(TAG, "onTimeSelect: ${AirUtils.getTime(date!!)}")
                    val date = AirUtils.getTime(date)
                    start_date.value.text = date?.substring(0, date.lastIndexOf(":"))
                    start_date.setExpand(false)
                }).setType(booleanArrayOf(true, true, true, true, true, false))
                        .setItemVisibleCount(5)
                        .setLineSpacingMultiplier(5.0f)
                        .isAlphaGradient(true)
                        .setContentTextSize(18)
                        .setTitleSize(18)
                        .setOutSideCancelable(true)
                        .isCyclic(true)
                        .setSubmitColor(Color.parseColor("#40C1B0"))
                        .setCancelColor(Color.parseColor("#333333"))
                        .setTitleBgColor(Color.parseColor("#ffffff"))
                        .setBgColor(Color.parseColor("#ffffff"))
                        .setTitleSize(30)
                        .setTitleText("Title")
                        .setTitleColor(Color.parseColor("#ffffff"))
                        .setTextColorCenter(Color.parseColor("#40C1B0"))
                        .addOnCancelClickListener{
                            start_date.setExpand(false)
                        }
                        .build().show()
            }
            override fun onShrink() {
                ///stationDialog?.dismiss()
            }
        })

        //结束时间
        end_date.value.text = endDate
        end_date.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                TimePickerBuilder(this@AQICurveActivity, OnTimeSelectListener {
                    date, _ ->
                    Log.i(TAG, "onTimeSelect: ${AirUtils.getTime(date!!)}")
                    val date = AirUtils.getTime(date)
                    end_date.value.text = date?.substring(0, date.lastIndexOf(":"))
                    end_date.setExpand(false)
                }).setType(booleanArrayOf(true, true, true, true, true, false))
                        .setItemVisibleCount(5)
                        .setLineSpacingMultiplier(5.0f)
                        .isAlphaGradient(true)
                        .setContentTextSize(18)
                        .setTitleSize(18)
                        .setOutSideCancelable(true)
                        .isCyclic(true)
                        .setSubmitColor(Color.parseColor("#40C1B0"))
                        .setCancelColor(Color.parseColor("#333333"))
                        .setTitleBgColor(Color.parseColor("#ffffff"))
                        .setBgColor(Color.parseColor("#ffffff"))
                        .setTitleSize(30)
                        .setTitleText("Title")
                        .setTitleColor(Color.parseColor("#ffffff"))
                        .setTextColorCenter(Color.parseColor("#40C1B0"))
                        .addOnCancelClickListener{
                            end_date.setExpand(false)
                        }
                        .build().show()
            }
            override fun onShrink() {
                ///stationDialog?.dismiss()
            }
        })

        //监测站点
        historyPoint.value.text = resources.getString(R.string.get_stations_loading)
        var stationDialog: ChoosePointDialog? = null
        historyPoint.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onExpand() {
                if (mProvinces.size == 0){
                    Toast.makeText(this@AQICurveActivity, "站点信息还没有获取到 稍后再试", Toast.LENGTH_SHORT).show()
                    historyPoint.setExpand(false)
                }else{
                    stationDialog = ChoosePointDialog(this@AQICurveActivity
                            , getString(R.string.station)
                            , mProvinces,mSelectProvince,mSelectCity,mSelectCounty,mSelectStation
                            , object : ChoosePointDialog.ConfirmPointListener {
                        @SuppressLint("SetTextI18n")
                        override fun onConfirm(selectProvince: TestPoint?,
                                               selectCity: CityPoint?,
                                               selectCounty: CountyPoint?,
                                               selectStation: TestPointDetail?) {
                            mSelectProvince = selectProvince
                            mSelectCity = selectCity
                            mSelectStation = selectStation
                            mSelectCounty = selectCounty
                            if (mSelectStation == null){
                                historyPoint.value.text =
                                        "${mSelectCounty?.title}/全部"
                            }else{
                                historyPoint.value.text =
                                        "${mSelectCounty?.title}/${mSelectStation?.title}"
                            }
                            loadEchartLayout()
                            historyPoint.setExpand(false)
                        }
                        override fun onCancel() {
                            historyPoint.setExpand(false)
                        }
                    })
                    stationDialog?.show()
                }
            }
            override fun onShrink() {
                stationDialog?.dismiss()
            }
        })

        //点击查询按钮
        getDatas.setOnClickListener {
            if(mSelectStation == null){
                Toast.makeText(this@AQICurveActivity
                        , this@AQICurveActivity.resources.getString(R.string.get_stations_fail)
                        , Toast.LENGTH_SHORT).show()
            }else{
                loadEchartLayout()
            }
        }
    }
    /**
     *  加载图表
     */
    private fun loadEchartLayout() {
        getAQIDataCurve()
        chartView.setDataSource(this)
        mAQIDataCurve?.data?.clear()
        loading.showContent()
    }

    /**
     * 获取当前的日期和时间
     */
    @SuppressLint("SimpleDateFormat")
    private fun initDateTime() {
        val endDateTime = Date()
        val startDateTime = Date(System.currentTimeMillis()-24*60*60*1000)
        val sdf = SimpleDateFormat("yyyy/MM/dd kk:mm")
        startDate = sdf.format(startDateTime)
        endDate = sdf.format(endDateTime)
    }

    /**
     * 查询AQI曲线数据
     */
    private fun getAQIDataCurve(){
        Log.i(TAG, "initData: getHistoricalData")
        val conditionParams = HashMap<String, Any>()
        if (mSelectStation != null){
            conditionParams[AirConstants.ID] = mSelectStation?.key.toString()
        }
        conditionParams[AirConstants.POLLUTIONTYPE] = "air"

        val timeParams = HashMap<String,Any>()
        timeParams[AirConstants.STARTIME] = start_date.value.text.toString()
        timeParams[AirConstants.ENDTIME] = end_date.value.text.toString()
        val params = HashMap<String, String>()
        params[AirConstants.CONDITION] = AirUtils.mapTojson(conditionParams)
        params[AirConstants.TIME] = AirUtils.mapTojson(timeParams)

        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_AQI_DATA_Curve)
                        , params
                        , object : GsonResponseHandler<WanResponse<AQIDataPoint>>() {
                    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    override fun onSuccess(statusCode: Int, d: WanResponse<AQIDataPoint>?) {
                        Log.i(TAG, "onSuccess: 信息获取成功")
                        if (statusCode == AirConstants.RESPONSE_STATUS_SUCCESS){
                            Log.i(TAG,"d.data:${d}")
                            if (d != null){
                                mAQIDataCurve = d.data
                                loadPage("file:///android_asset/echart/biz/echart-0.html")
                                //chartView.loadUrl("file:///android_asset/echart/biz/echart-0.html")
                            }else{
                                Toast.makeText(mContext
                                        , mContext?.resources?.getString(R.string.get_AQI_null)
                                        , Toast.LENGTH_SHORT).show()
                            }
                        }else if (statusCode == AirConstants.RESPONSE_STATUS_FAILURE){
                            Toast.makeText(mContext
                                    , mContext?.resources?.getString(R.string.get_AQI_fail)
                                    , Toast.LENGTH_SHORT).show()
                        }
                    }

                    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Toast.makeText(mContext
                                , mContext?.resources?.getString(R.string.get_historical_fail)
                                , Toast.LENGTH_SHORT).show()
                    }
                })
    }
    override fun markChartOptions(): GsonOption {
        return getLineChartOptions()
    }

    /**
     * AQI曲线展示
     */
    private fun getLineChartOptions(): GsonOption {
        rangehigh = mAQIDataCurve!!.rangehigh
        rangelow = mAQIDataCurve!!.rangelow
        var highValue = rangehigh + 1

        val option = GsonOption()
        //标签
        option.tooltip().trigger(Trigger.axis).formatter("{b}<br/>AQI : {c}")
        //颜色
        option.color("#4ca7ff")

        val line = Line("AQI").smooth(true)
        val categoryAxis = CategoryAxis()
        //数据填充
        for (item  in mAQIDataCurve?.data!!) {
            val itemx = timeStamp2Date(item.x.substring(0,10),"yyyy-MM-dd HH:mm")//(item.x)
            if(item.y == null){
                categoryAxis.data(itemx)
                line.data("---")
            }else{
                categoryAxis.data(itemx)
                if (highValue < item.y.toInt()){
                    highValue = item.y.toInt()
                }
                line.data(item.y)
            }
        }
        //最大(小)值
        val markPoint = MarkPoint()
        markPoint.data(MarkPointData("max","最大值"), MarkPointData("min","最小值"))
        line.markPoint(markPoint)
        //标线（平均值、上下限）
        val markLine = MarkLine()
        val label = Label("middle",true,"{b}:{c}")
        markLine.data(
                MarkAve("平均值","average",label),
                MarkRang("标准上限值", "$rangehigh",label),
                MarkRang("标准下限值","$rangelow",label)
                )
        markLine.itemStyle().normal().lineStyle().color("#A5A5A5")

        line.markLine(markLine)

        option.xAxis(categoryAxis.boundaryGap(false))
        option.yAxis(ValueAxis().name("AQI").max(highValue))
        option.series(line)
        //坐标轴刻度标签显示位置
        option.grid().containLabel = true
        option.grid().left(30)
        option.grid().right(30)

        option.exportToHtml("line.html")
        option.view()
        return option
    }

    override fun initIntentData() {}

    override fun initData() {}

    /**
     * 获取站点信息，并根据站点信息获取表头信息
     */
    private fun initPoint(){
        //取站点信息
        Log.i(TAG, "initData: Point")
        val params = HashMap<String, String>()
        params[AirConstants.TYPE] = ""
        OkHttpUtils.getInstance()
                .post(this, HttpUtil.getAirServiceAddress(AirConstants.GET_AIR_STATION_TREE)
                        , params
                        , object : GsonResponseHandler<WanResponse<ArrayList<TestPoint>>>() {
                    override fun onSuccess(statusCode: Int, d: WanResponse<ArrayList<TestPoint>>?) {
                        Toast.makeText(mContext
                                , mContext?.resources?.getString(R.string.get_stations_success)
                                , Toast.LENGTH_SHORT).show()
                        handlePoint(d)
                    }

                    override fun onFailure(statusCode: Int, error_msg: String?) {
                        Log.i(TAG, "onFailure: 站点信息获取失败")
                        Toast.makeText(mContext
                                , mContext?.resources?.getString(R.string.get_stations_fail)
                                , Toast.LENGTH_SHORT).show()
                    }
                })
    }

    /**
     * 处理监测点数据
     * */
    @SuppressLint("SetTextI18n")
    private fun handlePoint(d: WanResponse<ArrayList<TestPoint>>?) {
        d?.let {
            mProvinces = d.data
            //默认选择的站点
            mSelectProvince = d.data[0]
            if (mSelectProvince != null){
                mSelectCity = mSelectProvince!!.children?.get(0)
            }
            if (mSelectCity != null){
                mSelectCounty = mSelectCity!!.children?.get(0)
            }
            if (mSelectCounty != null){
                mSelectStation = mSelectCounty!!.children?.get(0)
                historyPoint.value.text =
                        "${mSelectCounty?.title}/${mSelectStation?.title}"
                Log.i(TAG, "mSelectStation:${mSelectStation?.key.toString()}")
                //在获取站点数据后执行自动刷新
                loadEchartLayout()
            }else{
                historyPoint.value.text =
                        "暂无站点可选"
            }
        }
    }

    override fun initToolbar() {
        toolbar.title.text = resources.getString(R.string.aqi_curve)
        toolbar.back.setImageResource(R.mipmap.toolbar_back)
        toolbar.back.setOnClickListener {
            finish()
        }
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private fun timeStamp2Date(seconds: String?, format: String?): String {
        var format = format
        if (seconds == null || seconds.isEmpty() || seconds == "null") {
            return ""
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss"
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date(java.lang.Long.valueOf(seconds + "000")))
    }
}


