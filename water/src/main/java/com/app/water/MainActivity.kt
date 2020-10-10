package com.app.water

import android.content.res.Configuration
import android.graphics.Color
import android.util.Log
import androidx.fragment.app.Fragment
import com.app.water.base.BaseActivity
import com.app.water.data.DataFragment
import com.app.water.monitor.MonitorFragment
import com.app.water.setting.SettingFragment
import com.app.water.statistic.StatisticFragment
import com.app.water.warn.WarnFragment
import com.next.easynavigation.view.EasyNavigationBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onConfigurationChanged(newConfig: Configuration) {
        //横竖屏切换的时保存数据
        super.onConfigurationChanged(newConfig)
    }

    companion object{
        const val TAG = "MainActivity"
    }

    private val monitorFragment by lazy { MonitorFragment.newInstance() }
    private val dataFragment by lazy { DataFragment.newInstance() }
    private val warnFragment by lazy { WarnFragment.newInstance() }
    private val statisticFragment by lazy { StatisticFragment.newInstance() }
    private val settingFragment by lazy { SettingFragment.newInstance() }

    private val fragmentList = arrayListOf<Fragment>()
    private val tabText = arrayOfNulls<String>(5)
    private val normalIcon = IntArray(5)
    private val selectIcon = IntArray(5)

    init {
        Log.i(TAG, ":init ")
        tabText[0] = "监控"
        tabText[1] = "数据"
        tabText[2] = "报警"
        tabText[3] = "统计"
        tabText[4] = "设置"
        normalIcon[0] = R.mipmap.monitor
        normalIcon[1] = R.mipmap.data
        normalIcon[2] = R.mipmap.warning
        normalIcon[3] = R.mipmap.chart
        normalIcon[4] = R.mipmap.setting
        selectIcon[0] = R.mipmap.monitor_click
        selectIcon[1] = R.mipmap.data_click
        selectIcon[2] = R.mipmap.warning_click
        selectIcon[3] = R.mipmap.chart_click
        selectIcon[4] = R.mipmap.setting_click
        fragmentList.apply {
            add(monitorFragment)
            add(dataFragment)
            add(warnFragment)
            add(statisticFragment)
            add(settingFragment)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        Log.i("test", "initView: ")
        navigationBar.titleItems(tabText)
            .normalIconItems(normalIcon)
            .selectIconItems(selectIcon)
            .fragmentList(fragmentList)
            .mode(EasyNavigationBar.NavigationMode.MODE_NORMAL)
            .fragmentManager(supportFragmentManager)
            .navigationBackground(Color.parseColor("#ffffff"))
            .normalTextColor(Color.parseColor("#9A9A9A"))
            .selectTextColor(Color.parseColor("#4CA8FF"))
            .iconSize(18)
            .tabTextSize(10)
            .tabTextTop(5)
            .build()
    }

    override fun initToolbar() {
    }

    override fun initData() {
    }

}
