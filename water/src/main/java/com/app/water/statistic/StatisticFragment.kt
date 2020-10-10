package com.app.water.statistic
import com.app.water.R
import com.app.water.base.BaseFragment
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.fragment_monitor.*


class StatisticFragment : BaseFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fragment_monitor
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initToolbar() {
        toolbar.title.text = "统计"
    }

    companion object {
        @JvmStatic
        fun newInstance() = StatisticFragment()
    }

}
