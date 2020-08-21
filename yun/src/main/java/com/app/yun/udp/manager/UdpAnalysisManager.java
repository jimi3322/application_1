package com.app.yun.udp.manager;

import android.util.Log;

import com.app.yun.udp.business.ResultProcessor;

public class UdpAnalysisManager {

    private static final String TAG = "UdpAnalysisManager";
    private static UdpAnalysisManager instance;

    private UdpAnalysisManager() {
    }

    public static UdpAnalysisManager getInstance() {
        if (instance == null) {
            synchronized (UdpAnalysisManager.class) {
                if (instance == null) {
                    instance = new UdpAnalysisManager();
                }
            }
        }
        return instance;
    }

    public void handle(String receiveMsg) {
        Log.i(TAG, "handle: type = " + getMsgType(receiveMsg));
        ResultProcessor.getInstance().onProcessMsg(receiveMsg,getMsgType(receiveMsg));
    }

    private String getMsgType(String receiveMsg) {
        String msgType = "";
        //从方法返回一个指定字符串值最后出现的位置，在一个字符串从后向前搜索
        int startIndex = receiveMsg.lastIndexOf("#");
        int endIndex = receiveMsg.lastIndexOf(";");
        final String headData = receiveMsg.substring(0, endIndex + 1);
        /**
         * split此方法用于字符串分解，得到一个字符串数组如：
        String[] arr = "11,22,33,44".splite(",");
        从而方便的得到一个字符串数组：arr={"11", "22", "33", "44"};*/
        String[] headString = headData.split(";");
        for (int i = 0; i < headString.length; i++){
            if (headString[i].startsWith("CN=")){
                msgType =  headString[i].substring(headString[i].length() - 4,headString[i].length());
            }
        }
        return msgType;
    }
}
