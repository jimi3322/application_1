package com.wayeal.newair.statistic

import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import com.wayeal.newair.R
import com.wayeal.newair.base.BaseActivity
import kotlinx.android.synthetic.main.activity_air_quality_ranking.toolbar
import kotlinx.android.synthetic.main.activity_comparative_analysis.*
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.dialog_list.view.title

class ComparativeAnalysisActivity : BaseActivity() {

    private lateinit var mSingleAnalysisFragment: SingleAnalysisFragment
    private lateinit var mMutliAnalysisFragment: MutliAnalysisFragment

    override fun getLayoutId(): Int {
        return R.layout.activity_comparative_analysis
    }

    override fun initView() {
        initFragment()
        initRadioGroup()
        showFragment(mSingleAnalysisFragment)
    }

    override fun initIntentData() {
    }

    private fun initFragment(){
        mSingleAnalysisFragment = SingleAnalysisFragment()
        mMutliAnalysisFragment = MutliAnalysisFragment()
    }

    private fun initRadioGroup(){
        rbSingle.setOnClickListener {
            rbSingle.setTextColor(resources.getColor(R.color.selectedText))
            rbMutli.setTextColor(resources.getColor(R.color.unSelectedText))
            showFragment(mSingleAnalysisFragment)
        }
        rbMutli.setOnClickListener {
            rbMutli.setTextColor(resources.getColor(R.color.selectedText))
            rbSingle.setTextColor(resources.getColor(R.color.unSelectedText))
            showFragment(mMutliAnalysisFragment)
        }
    }

    override fun initToolbar() {
        toolbar.title.text = resources.getString(R.string.compair_analysis)
        toolbar.back.visibility = View.VISIBLE
        toolbar.back.setOnClickListener {
            finish()
        }
    }

    override fun initData() {
    }

    private fun showFragment(fragment: Fragment) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        if (mSingleAnalysisFragment !== fragment) {
            beginTransaction.hide(mSingleAnalysisFragment)
        }
        if (mMutliAnalysisFragment !== fragment) {
            if (mMutliAnalysisFragment.isVisible){
                beginTransaction.hide(mMutliAnalysisFragment)
            }
        }
        if (fragment.isAdded) {
            beginTransaction.show(fragment)
        } else {
            Log.i(TAG, "showFragment: test")
            beginTransaction.add(R.id.analysisContainer, fragment)
        }
        beginTransaction.commit()
    }

}