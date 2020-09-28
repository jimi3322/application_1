package com.app.yun.fragment

import android.content.Context
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.app.myapplication.basemvp.BaseMVPFragment
import com.app.yun.R
import com.app.yun.adpter.ProjectPagerAdapter
import com.app.yun.bean.ProjectPageItem
import com.app.yun.contract.ProjectContract
import com.app.yun.present.ProjectPresenter
import com.google.android.material.tabs.TabLayout


class TabMvpEventbusFragment : BaseMVPFragment<ProjectPresenter>(), ProjectContract.View {
    private var mFakeStatusBar: View? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun createPresenter(): ProjectPresenter {
        return ProjectPresenter()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_project
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun initView(rootView: View?) {
        mFakeStatusBar?.post(Runnable {
            val height = mFakeStatusBar?.getHeight()
            Log.e("debufgdbug", "run: $height")
        })
    }

    override fun initData() {
        presenter.getProjectTabs()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onProjectTabs(projectPageItemList: MutableList<ProjectPageItem>?) {
        val pagerAdapter = ProjectPagerAdapter(childFragmentManager)
        pagerAdapter.setPages(projectPageItemList)
        viewPager?.setAdapter(pagerAdapter)
        tabLayout?.setupWithViewPager(viewPager)
    }

    fun setStatusBarColor(color: Int) {
        mFakeStatusBar?.setBackgroundColor(color)
    }
}