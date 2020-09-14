package com.wayeal.newair.data

import android.content.Intent
import com.wayeal.newair.R
import com.wayeal.newair.base.BaseFragment
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.fragment_data.*

class DataFragment : BaseFragment() {


    override fun getLayoutResId(): Int {
        return R.layout.fragment_data
    }

    override fun initView() {
        historicalData.setOnClickListener {
            startActivity(Intent(mActivity,HistoricalDataActivity::class.java))
        }
        dayData.setOnClickListener {
            startActivity(Intent(mActivity,DayDataActivity::class.java))
        }
        historicalDataCurve.setOnClickListener {
            startActivity(Intent(mActivity,HistoricalDataCurveActivity::class.java))
        }
        aqiCurve.setOnClickListener {
            startActivity(Intent(mActivity,AQICurveActivity::class.java))
        }
    }

    override fun initData() {
    }

    override fun initToolbar() {
        toolbar.title.text = "数据"
    }

    companion object {
        @JvmStatic
        fun newInstance() = DataFragment()
    }


}