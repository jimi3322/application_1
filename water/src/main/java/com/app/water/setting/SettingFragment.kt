package com.app.water.setting
import com.app.water.R
import com.app.water.base.BaseFragment
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.fragment_monitor.*


class SettingFragment : BaseFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fragment_monitor
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initToolbar() {
        toolbar.title.text = "设置"
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingFragment()
    }

}
