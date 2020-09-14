package com.wayeal.newair.setting

import android.content.Intent
import android.os.Bundle
import com.wayeal.newair.R
import com.wayeal.newair.base.BaseFragment
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.fragment_data.toolbar
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : BaseFragment() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_setting
    }

    override fun initView() {
        aqiexplain.setOnClickListener {
            startActivity(Intent(mActivity,AQIExplainActivity::class.java))
        }
    }

    override fun initData() {
    }

    override fun initToolbar() {
        toolbar.title.text = "设置"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                SettingFragment()
    }
}