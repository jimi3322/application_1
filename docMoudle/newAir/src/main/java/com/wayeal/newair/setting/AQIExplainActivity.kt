package com.wayeal.newair.setting


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.wayeal.newair.R
import com.wayeal.newair.base.BaseActivity
import com.wayeal.newair.common.SpaceItemDecoration
import com.wayeal.newair.setting.adapter.ExplainDataAdapter
import com.wayeal.newair.setting.bean.ExplainDatas
import kotlinx.android.synthetic.main.activity_aqi_explain.*
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.common_blue_tablayout.view.title
import kotlinx.android.synthetic.main.fragment_statistic.toolbar
import kotlin.collections.ArrayList


class AQIExplainActivity : BaseActivity() {
    private var mExplainDataList: ArrayList<ExplainDatas>? = ArrayList()
    private var mAdapter: ExplainDataAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_aqi_explain
    }

    override fun initView() {
        initRecyclerView()
    }

    /**
     * recyclerView初始化
     * */
    private fun initRecyclerView() {
        Log.i(com.wayeal.newair.data.TAG, "init: initRecyclerView")
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerExplain.layoutManager = linearLayoutManager
        mAdapter = ExplainDataAdapter(this)
        recyclerExplain.addItemDecoration(SpaceItemDecoration(bottom = 5))
        recyclerExplain.adapter = mAdapter
        mAdapter?.setData(mExplainDataList!!)
    }

    override fun initIntentData() {

    }
    override fun initData() {
        mExplainDataList?.add(ExplainDatas("(优，0-50)","rgb(0,228,0)","空气质量令人满意，基本无空气污染","各类人群可正常活动"))
        mExplainDataList?.add(ExplainDatas("(良，51-100)","rgb(255,255,0)","空气质量可接受，但某些污染物可能对极少数异常敏感人群健康有较弱影响","极少数异常敏感人群应减少户外活动"))
        mExplainDataList?.add(ExplainDatas("(轻度污染，101-150)","rgb(255,126,0)","易感人群症状有轻度加剧，健康人群出现刺激症状","儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间高强度的户外锻炼"))
        mExplainDataList?.add(ExplainDatas("(中度污染，151-200)","rgb(255,0,0)","进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响","儿童、老年人及心脏病、呼吸系统疾病患者应减少长时间高强度的户外锻炼，一般人群适量减少户外运动"))
        mExplainDataList?.add(ExplainDatas("(重度污染，201-300)","rgb(153,0,75)","心脏病和肺病患者症状显著加剧，运动耐受力降低，健康人群中普遍出现症状","儿童、老年人及心脏病、肺病患者应停留在室内，停止户外活动，一般人群应减少户外活动"))
        mExplainDataList?.add(ExplainDatas("(严重污染，>300)","rgb(126,0,35)","健康人运动耐受力降低，有明显强烈症状，提前出现某些疾病","儿童、老年人和病人应停留在室内，避免体力消耗，一般人群应避免户外活动"))

    }

    override fun initToolbar() {
        toolbar.title.text = resources.getString(R.string.setting_AQI)
        toolbar.back.setImageResource(R.mipmap.toolbar_back)
        toolbar.back.setOnClickListener {
            finish()
        }
    }

}


