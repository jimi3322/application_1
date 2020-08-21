package com.app.myapplication.common.utils.http;

/**
 * Created by WUJINGWEI on 2018/8/22.
 */

import com.app.myapplication.common.domain.Constant;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 基于HttpUrlConnection实现的Http工具类
 */
public class HttpUtil {

    /**
     * 连接超时毫秒数
     */
    private static final int CONNECT_TIME_OUT = 8000;
    /**
     * 读取数据超时毫秒数
     */
    private static final int READ_TIME_OUT = 8000;
    /**
     * 服务器返回的cookie内容
     */
    private static String responseCookie;
    public static String RES_SP_KEY = "responseCookie";

    /**
     * 通过Http的Get方法发送Json数据
     *
     * @param address  服务端地址
     * @param listener 回调监听器
     */
    public static void sendGetHttpRequest(final String address, final HttpCallbackListener listener) {
        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL(address);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECT_TIME_OUT);
            connection.setReadTimeout(READ_TIME_OUT);
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            if (listener != null) {
                listener.onFinish(response.toString());
            }
        } catch (Exception e) {
            listener.onError(e);
        }
    }

    /**
     * Http回调监听接口
     */
    public interface HttpCallbackListener {
        /**
         * 回调结束触发
         *
         * @param response 返回响应字符串
         */
        void onFinish(String response);

        /**
         * 回调出错触发
         *
         * @param e 返回异常对象
         */
        void onError(Exception e);
    }

    /**
     * 获取服务完整地址
     *
     * @param serviceMethod 服务方法名
     * @return 服务完整地址
     */
    public static String getServiceAddress(String serviceMethod) {
        String ip = Constant.NODE_SERVICE_IP;
        String port = Constant.NODE_SERVICE_PORT;
        return Constant.HTTP_HEAD + ip + Constant.ID_COLON_EN + port+Constant.NODE_SERVICE_API + Constant.ID_LEFT_SLASH + serviceMethod;
    }



    /**
     * 云平台
     * @param serviceMethod
     * @return
     */
    public static String getWyServiceAddress(String serviceMethod) {
        String ip = Constant.NODE_SERVICE_IP;
        String port = Constant.NODE_SERVICE_PORT;
        return Constant.HTTP_HEAD + ip + Constant.ID_COLON_EN + port+Constant.WY_NODE_SERVICE_API + Constant.ID_LEFT_SLASH + serviceMethod;
    }

    /**
     * 云平台
     * @param serviceMethod
     * @return
     */
    public static String getIotServiceAddress(String serviceMethod) {
        String ip = Constant.NODE_SERVICE_IP;
        String port = Constant.NODE_SERVICE_PORT;
        return Constant.HTTP_HEAD + ip + Constant.ID_COLON_EN + port+Constant.IOT_NODE_SERVICE_API + Constant.ID_LEFT_SLASH + serviceMethod;
    }
}

