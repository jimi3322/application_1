package com.wayeal.newair.monitor

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
import kotlinx.android.synthetic.main.fragment_monitor.*

class MonitorFragment : BaseFragment() {


    override fun getLayoutResId(): Int {
        return R.layout.fragment_monitor
    }

    override fun initView() {
        realTimeList.setOnClickListener {
            startActivity(Intent(mActivity,RealTimeListActivity::class.java))
        }
        realTimeData.setOnClickListener {
            startActivity(Intent(mActivity,RealTimeDataActivity::class.java))
        }
        gisMap.setOnClickListener {
            startActivity(Intent(mActivity,GisMapActivity::class.java))
        }
    }

    override fun initData() {

    }

    override fun initToolbar() {
        toolbar.title.text = "监控"
    }

    companion object {
        @JvmStatic
        fun newInstance() = MonitorFragment()
    }


}