package com.app.yun.udp.business;

import android.util.Log;

import com.app.common.data.AlarmData;
import com.app.common.data.CalibrationPositionData;
import com.app.common.data.LeakListData;
import com.app.common.data.MainData;
import com.app.common.data.OutputData;
import com.app.common.data.ParamsData;
import com.app.common.data.VoltageData;
import com.app.common.data.WorkStateData;
import com.app.yun.data.DeviceInfoData;
import com.app.yun.udp.ProtocolConstant;
import com.app.yun.udp.listener.IResultInfoListner;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultProcessor implements IMsgProcessor {

    private static final String TAG = "MainDataProcessor";
    private static ResultProcessor mInstance = null;
    private List<IResultInfoListner> mListeners = new ArrayList<IResultInfoListner>();

    private ResultProcessor() {
    }

    public static ResultProcessor getInstance() {
        if (mInstance == null) {
            synchronized (ResultProcessor.class) {
                if (mInstance == null) {
                    mInstance = new ResultProcessor();
                }
            }
        }
        return mInstance;
    }

    @Override
    public void init() {

    }

    @Override
    public void onProcessMsg(String msg) {
        Log.d(TAG, "onProcessMsg: msg = " + msg);
        MainData data = null;
        try {
            int startIndex = msg.indexOf("{");
            int endIndex = msg.lastIndexOf("}");
            final String resultData = msg.substring(startIndex, endIndex + 1);
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(resultData);
            int code = jsonObject.getInt("code");
            switch (code) {
                case 0: {
                    String tempStr = resultData.substring(1, resultData.length());
                    String resultStr = tempStr.substring(tempStr.indexOf("{"), tempStr.length() - 1);
                    data = gson.fromJson(resultStr, MainData.class);
                    break;
                }
                case 1:{
                    Log.e(TAG, "onProcessMsg: 数据错误");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onProcessMsg(String msg, String type) {
        int startIndex = msg.indexOf("{");
        int endIndex = msg.lastIndexOf("}");
        final String resultData = msg.substring(startIndex, endIndex + 1);
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        int code = -1;
        try {
            jsonObject = new JSONObject(resultData);
            code = jsonObject.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String tempStr = resultData.substring(1, resultData.length());
        String resultStr = tempStr.substring(tempStr.indexOf("{"), tempStr.length() - 1);
        if (code == 0){
            switch (type){
                case ProtocolConstant.DataList:{
                    LeakListData data = gson.fromJson(resultStr,LeakListData.class);
                    handleListener(type,data);
                    break;
                }
                case ProtocolConstant.MainView:{
                    try{
                        MainData data = gson.fromJson(resultStr,MainData.class);
                        handleListener(type,data);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case ProtocolConstant.ListDevice:{
                    DeviceInfoData data = gson.fromJson(resultStr,DeviceInfoData.class);
                    handleListener(type,data);
                    break;
                }
                case ProtocolConstant.OutPut:{
                    OutputData data = gson.fromJson(resultStr,OutputData.class);
                    handleListener(type,data);
                    break;
                }
                case ProtocolConstant.CalibrationPosition:{
                    CalibrationPositionData data = gson.fromJson(resultStr,CalibrationPositionData.class);
                    handleListener(type,data);
                    break;
                }
                case ProtocolConstant.ParamsData:{
                    ParamsData data = gson.fromJson(resultStr,ParamsData.class);
                    handleListener(type,data);
                    break;
                }
                case ProtocolConstant.AlarmData:{
                    AlarmData data = gson.fromJson(resultStr,AlarmData.class);
                    handleListener(type,data);
                    break;
                }
                case ProtocolConstant.VoltageData:{
                    VoltageData data = gson.fromJson(resultStr,VoltageData.class);
                    handleListener(type,data);
                    break;
                }
                case ProtocolConstant.WorkState:{
                    WorkStateData data = gson.fromJson(resultStr,WorkStateData.class);
                    handleListener(type,data);
                    break;
                }
            }

        }
    }

    private void handleListener(String type, Object data){
        for (IResultInfoListner listner:mListeners){
            if (listner.getType().equals(type)){
                listner.onResult(data);
            }
        }
    }

    public void addListener(IResultInfoListner listener) {
        Log.i(TAG, "addListener: ");
        if (listener != null && !mListeners.contains(listener)) {
            Log.i(TAG, "addListener: add");
            mListeners.add(listener);
        }
    }

    public void removeListener(IResultInfoListner listener) {
        Log.i(TAG, "removeListener: ");
        if (listener != null) {
            Log.i(TAG, "removeListener: remove");
            mListeners.remove(listener);
        }
    }
}
