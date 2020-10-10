package com.app.water.warn
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import com.app.water.R
import com.app.water.common.BaseBussinessFragment
import com.app.water.common.view.ChooseView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.wayeal.newair.common.WaterUtils
import kotlinx.android.synthetic.main.common_blue_tablayout.view.*
import kotlinx.android.synthetic.main.fragment_monitor.toolbar
import kotlinx.android.synthetic.main.fragment_warn.*
import java.util.*

const val TAG = "WarnFragment"
class WarnFragment : BaseBussinessFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fragment_warn
    }

    override fun initData() {
    }

    override fun initToolbar() {
        toolbar.title.text = "警告"
    }

    companion object {
        @JvmStatic
        fun newInstance() = WarnFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    override fun initView(){
        initItemView()
//        initRecyclerView()
//        initRefreshLayout()
    }

    protected lateinit var stDate: Date       //开始时间
    protected lateinit var edDate: Date       //结束时间

    private fun initItemView(){
        //开始时间
        stDate = Date(System.currentTimeMillis() - 24 * 3600 * 1000)
        startTime.setValue(WaterUtils.getTime(stDate))
        val startTimeDialog = TimePickerBuilder(activity, OnTimeSelectListener {
                date, _ ->
            Log.i(TAG, "onTimeSelect: ${WaterUtils.getTime(date!!)}")
            startTime.setExpand(false)
            stDate = date
            startTime.setValue(WaterUtils.getTime(date))
        }).setType(booleanArrayOf(true, true, true, true, true, false))
            .setItemVisibleCount(5)
            .setLineSpacingMultiplier(5.0f)
            .isAlphaGradient(true)
            .setContentTextSize(18)
            .setTitleSize(18)
            .setOutSideCancelable(true)
            .isCyclic(true)
            .setSubmitColor(Color.parseColor("#40C1B0"))
            .setCancelColor(Color.parseColor("#333333"))
            .setTitleBgColor(Color.parseColor("#ffffff"))
            .setBgColor(Color.parseColor("#ffffff"))
            .setTitleSize(30)
            .setTitleText("Title")
            .setTitleColor(Color.parseColor("#ffffff"))
            .setTextColorCenter(Color.parseColor("#40C1B0"))
            .setDate(WaterUtils.Date2Calendar(stDate))
            .addOnCancelClickListener {
                startTime.setExpand(false)
            }
            .build()
        startTime.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onShrink() {
                startTimeDialog.dismiss()
            }

            override fun onExpand() {
                startTimeDialog.show()
            }
        })

        //结束时间
        edDate = Date(System.currentTimeMillis())
        endTime.setValue(WaterUtils.getTime(edDate))
        val endTimeDialog = TimePickerBuilder(activity, OnTimeSelectListener {
                date, _ ->
            Log.i(TAG, "onTimeSelect: ${WaterUtils.getTime(date!!)}")
            endTime.setExpand(false)
            stDate = date
            endTime.setValue(WaterUtils.getTime(date))
        }).setType(booleanArrayOf(true, true, true, true, true, false))
            .setItemVisibleCount(5)
            .setLineSpacingMultiplier(5.0f)
            .isAlphaGradient(true)
            .setContentTextSize(18)
            .setTitleSize(18)
            .setOutSideCancelable(true)
            .isCyclic(true)
            .setSubmitColor(Color.parseColor("#40C1B0"))
            .setCancelColor(Color.parseColor("#333333"))
            .setTitleBgColor(Color.parseColor("#ffffff"))
            .setBgColor(Color.parseColor("#ffffff"))
            .setTitleSize(30)
            .setTitleText("Title")
            .setTitleColor(Color.parseColor("#ffffff"))
            .setTextColorCenter(Color.parseColor("#40C1B0"))
            .setDate(WaterUtils.Date2Calendar(stDate))
            .addOnCancelClickListener {
                endTime.setExpand(false)
            }
            .build()
        endTime.setExpandListener(object : ChooseView.OnChooseClickListener{
            override fun onShrink() {
                endTimeDialog.dismiss()
            }

            override fun onExpand() {
                endTimeDialog.show()
            }
        })
    }


}
