package com.app.yun.ui

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.common.data.HemassConstant
import com.app.myapplication.common.LogUtil.d
import com.app.yun.R
import com.app.yun.adpter.DeviceListAdapter
import com.app.yun.data.DeviceInfoData
import com.app.yun.udp.ProtocolConstant
import com.app.yun.udp.business.ResultProcessor
import com.app.yun.udp.listener.IResultInfoListner
import com.app.yun.udp.manager.UdpManager
import kotlinx.android.synthetic.main.activity_device.*
import java.util.*

class DeviceListActivity : AppCompatActivity(), IResultInfoListner<DeviceInfoData> {
    override fun getType(): String {
        return ProtocolConstant.ListDevice
    }
    private val TAG = "DeviceListActivity"
    private var refreshTimer: Timer? = null
    private val GET_REFRESH_TIMER_MESSAGE = 100
    private val UI_REFRESH = 101;

    private var mAdapter: DeviceListAdapter? = null

    private var mDeviceInfoData: DeviceInfoData? = null
    private val mDeviceInfos: ArrayList<DeviceInfoData> by lazy {
        ArrayList<DeviceInfoData>()
    }

    val mHandler =  object : Handler(){
        override fun handleMessage(msg: Message) {
            //在这可以进行UI操作
            when(msg.what){
                GET_REFRESH_TIMER_MESSAGE -> {
                    d("100","handleMessage send udp")
                   setUdpBroadcast()
                }
                UI_REFRESH -> {
                    Log.d("101","handleMessage ui refresh")
                    val b = msg.data
                    val device = b.getSerializable("device") as DeviceInfoData
                    handleData(device)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
        initView()
        ResultProcessor.getInstance().addListener(this)
    }
    override fun onResume() {
        super.onResume()
        startTimer(HemassConstant.interval * 60)
    }

    private fun initView(){
        val content = SpannableString(resources.getString(R.string.device_list_refresh_title))
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        refreshTV.text = content
        refreshTV.setOnClickListener {
//            showAutoCloseLoadingView("正在搜索设备中")
            startTimer(HemassConstant.interval * 60)
        }
    }

    private fun startTimer(period: Long){
        if (refreshTimer != null) {
            refreshTimer?.cancel()
            refreshTimer?.purge()
            refreshTimer = null
        }
        refreshTimer = Timer()
        refreshTimer?.schedule(object : TimerTask() {
            val message = mHandler.obtainMessage()
            override fun run() {
                d("Timer","startTimer Send get refresh")
                mDeviceInfos.clear()
                message.what = GET_REFRESH_TIMER_MESSAGE
                mHandler.sendMessage(message)
            }
        }, 0L, period * 1000)
    }
    /**
     * 停止定时器
     */
    private fun stopTimer() {
        refreshTimer?.cancel()
        refreshTimer?.purge()
        refreshTimer = null
    }

    override fun onStop() {
        super.onStop()
        stopTimer()
    }
    override fun onDestroy() {
        super.onDestroy()
        ResultProcessor.getInstance().removeListener(this)
    }


    /**
     * 发送udp广播
     */
    private fun setUdpBroadcast() {
        val param = HashMap<String, String>()
        param["CN"] = "1000"
        UdpManager.getInstance().setServerIp("255.255.255.255")
        UdpManager.getInstance().sendMsg(param)
    }

    //监听结果返回
    override fun onResult(deviceInfoData: DeviceInfoData?) {
//        dismissLoadingView()
        Log.i("onResult-deviceInfoData", deviceInfoData?.deviceName.toString())
        mDeviceInfoData = deviceInfoData
        val msg = Message()
        val bundle = Bundle()
        bundle.putSerializable("device",deviceInfoData)
        msg.what = UI_REFRESH
        msg.data = bundle
        mHandler.sendMessage(msg)
    }

    /**
     * 页面展示
     */
    private fun handleData(data: DeviceInfoData?) {
        Log.d(TAG,"handleData")
        if (data != null) {
//            refreshTV.visibility = View.GONE
            preHandleData(data)
            if (mAdapter == null) {
                mAdapter = DeviceListAdapter(mDeviceInfos)
                mAdapter?.setOnItemClickListener(object : DeviceListAdapter.OnItemClickListener {
                    override fun setOnclickItem(view: View, position: Int) {
                        Thread.sleep(500)
                        if (mAdapter?.getList() != null ){
                            if (mAdapter?.getList()!!.size > 0){
                                val itemInfo = mAdapter?.getList()!![position]
                                if (!TextUtils.isEmpty(itemInfo?.ip) && !TextUtils.isEmpty(itemInfo?.mn)) {
                                    UdpManager.getInstance().setServerIp(itemInfo?.ip)
                                   // HeMassMainActivity.invoke(this@DeviceListActivity, itemInfo?.ip, itemInfo?.mn)
                                }
                            }else{
                                Toast.makeText(this@DeviceListActivity,"请重新刷新", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this@DeviceListActivity,"请重新刷新", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                deviceList.adapter = mAdapter
            } else {
                mAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun preHandleData(data: DeviceInfoData?){
        Log.d(TAG,"新扫描出的设备名为 ${data?.deviceName}")
        var isNewData = true
        var existIndex = -1
        for (index in mDeviceInfos.indices){
            if (mDeviceInfos[index].deviceName.equals(data?.deviceName)){
                Log.d(TAG,"该数据已经存在")
                isNewData = false
                existIndex = index
            }
        }
        if (isNewData){
            Log.d(TAG,"新加设备")
            mDeviceInfos.add(data!!)
        }else{
            Log.d(TAG,"替换设备")
            mDeviceInfos.removeAt(existIndex)
            mDeviceInfos.add(data!!)
        }
    }


}
