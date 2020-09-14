package com.wayeal.newair.statistic

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.wayeal.newair.R
import com.wayeal.newair.base.BaseFragment
import com.wayeal.newair.common.AirUtils
import com.wayeal.newair.common.adapter.CommonTitleAdapter
import com.wayeal.newair.common.SpaceItemDecoration
import com.wayeal.newair.common.bean.TitleBean
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.fragment_statistic.*

class StatisticFragment : BaseFragment() {

    companion object {
        fun newInstance() = StatisticFragment()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_statistic
    }

    override fun initView() {
        calendar.setOnClickListener {
            startActivity(Intent(activity,AirQualityCalenderActivity::class.java))
        }
        ranking.setOnClickListener {
            startActivity(Intent(activity,AirQualityRankingActivity::class.java))
        }
        compareAnalysis.setOnClickListener {
            startActivity(Intent(activity,ComparativeAnalysisActivity::class.java))
        }
        quality_condition.setOnClickListener {
            startActivity(Intent(activity,QualityConditionActivity::class.java))
        }
    }

    override fun initData() {

    }

    override fun initToolbar() {
        toolbar.title.text = "统计"
    }
}