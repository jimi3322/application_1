package com.wayeal.newair.statistic

import android.support.v4.app.Fragment
import com.wayeal.newair.R
import com.wayeal.newair.base.BaseActivity
import kotlinx.android.synthetic.main.activity_quality_condition.*

class QualityConditionActivity : BaseActivity() {

    private lateinit var mQualityConditionStatisticFragment: QualityConditionStatisticFragment
    private lateinit var mQualityContemporaryCompareFragment: QualityContemporaryCompareFragment

    override fun getLayoutId(): Int {
        return R.layout.activity_quality_condition
    }

    override fun initView() {
        initFragment()
        initRadioGroup()
        showFragment(mQualityConditionStatisticFragment)
    }

    private fun initFragment(){
        mQualityConditionStatisticFragment = QualityConditionStatisticFragment()
        mQualityContemporaryCompareFragment = QualityContemporaryCompareFragment()
    }

    private fun initRadioGroup(){
        rbStatistic.setOnClickListener {
            rbStatistic.setTextColor(resources.getColor(R.color.selectedText))
            rbCompare.setTextColor(resources.getColor(R.color.unSelectedText))
            showFragment(mQualityConditionStatisticFragment)
        }
        rbCompare.setOnClickListener {
            rbCompare.setTextColor(resources.getColor(R.color.selectedText))
            rbStatistic.setTextColor(resources.getColor(R.color.unSelectedText))
            showFragment(mQualityContemporaryCompareFragment)
        }
    }

    override fun initToolbar() {

    }

    override fun initData() {

    }

    private fun showFragment(fragment: Fragment) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        if (mQualityConditionStatisticFragment !== fragment) {
            beginTransaction.hide(mQualityConditionStatisticFragment)
        }
        if (mQualityContemporaryCompareFragment !== fragment) {
            if (mQualityContemporaryCompareFragment.isVisible){
                beginTransaction.hide(mQualityContemporaryCompareFragment)
            }
        }
        if (fragment.isAdded) {
            beginTransaction.show(fragment)
        } else {
            beginTransaction.add(R.id.analysisContainer, fragment)
        }
        beginTransaction.commit()
    }

}