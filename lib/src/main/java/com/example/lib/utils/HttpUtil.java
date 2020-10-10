package com.example.lib.utils;

/**
 * Created by WUJINGWEI on 2018/8/22.
 */

import com.example.lib.domain.Constant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
     * 通过Http的Post方法发送Json数据
     *
     * @param jSessionID jSessionID字符串
     * @param address    服务地址
     * @param jsonBody   json数据体
     * @param listener   回调监听器
     */
    public static void sendPostHttpRequest(final String jSessionID, final String address, final String jsonBody, final HttpCallbackListener listener) {
        HttpURLConnection connection = null;
        URL url = null;
        String extraCookie = "";

        try {
            url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(CONNECT_TIME_OUT);
            connection.setReadTimeout(READ_TIME_OUT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //以前逻辑responseCookie 是有一个static String保存
            //存在问题：当页面崩溃时，现在登陆接口使用的是okhttp，不会调用这个方法，所以登陆之后还是为空
            //为了解决这个问题，在登陆成功之后，把cookie保存在sp中
            responseCookie = WySharePCache.loadStringCach(RES_SP_KEY);
            extraCookie += responseCookie == null ? "" : responseCookie;
            connection.setRequestProperty("Cookie", jSessionID + ";" + extraCookie); //附带服务器返回的cookie内容，否则不识别
            connection.setRequestProperty("Origin", ""); //解决跨域问题

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter requestBody = new BufferedWriter(new OutputStreamWriter(outputStream));
            requestBody.write(jsonBody);
            requestBody.flush();
            requestBody.close();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            responseCookie = connection.getHeaderField("Set-Cookie");// 取到服务器端返回的Cookie内容
            WySharePCache.saveStringCach(RES_SP_KEY,responseCookie);
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            if (listener != null) {
                listener.onFinish(response.toString());
            }

        } catch (Exception e) {
            if (listener != null) {
                listener.onError(e);
            }
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
     * 获取运维接口地址
     * @param serviceMethod
     * @return
     */
    public static String getOperationServiceAddress(String serviceMethod) {
        String ip = Constant.NODE_SERVICE_IP;
        String port = Constant.NODE_SERVICE_PORT;
        return Constant.HTTP_HEAD + ip + Constant.ID_COLON_EN + port+Constant.OPERATION_NODE_SERVICE_API + Constant.ID_LEFT_SLASH + serviceMethod;
    }

    /**
     * 获取空气站地址
     * @param serviceMethod
     * @return
     */
    public static String getAirServiceAddress(String serviceMethod) {
        String ip = Constant.NODE_SERVICE_IP;
        String port = Constant.NODE_SERVICE_PORT;
        return Constant.HTTP_HEAD + ip + Constant.ID_COLON_EN + port+Constant.AIR_NODE_SERVICE_API + Constant.ID_LEFT_SLASH + serviceMethod;
    }

    /**
     * 获取移动执法接口地址
     * @param serviceMethod
     * @return
     */
    public static String getLawEnforServiceAddress(String serviceMethod) {
        String ip = Constant.NODE_SERVICE_IP;
        String port = Constant.NODE_SERVICE_PORT;
        return Constant.HTTP_HEAD + ip + Constant.ID_COLON_EN + port+Constant.LAW_ENFORCEMENT_NODE_SERVICE_API + Constant.ID_LEFT_SLASH + serviceMethod;
    }

    /**
     * 获取污染源在线接口地址
     * @param serviceMethod
     * @return
     */
    public static String getCloudServiceAddress(String serviceMethod) {
        String ip = Constant.NODE_SERVICE_IP;
        String port = Constant.NODE_SERVICE_PORT;
        return Constant.HTTP_HEAD + ip + Constant.ID_COLON_EN + port+Constant.CLOUD_NODE_SERVICE_API + Constant.ID_LEFT_SLASH + serviceMethod;
    }

    /**
     * 获取水平台完整地址
     *
     * @param serviceMethod 服务方法名
     * @return 服务完整地址
     */
    public static String getWaterAddress(String serviceMethod) {
        String ip = Constant.NODE_SERVICE_IP;
        String port = Constant.NODE_SERVICE_PORT;
        return Constant.HTTP_HEAD + ip + Constant.ID_COLON_EN + port+Constant.WATER_NODE_SERVICE_API + Constant.ID_LEFT_SLASH + serviceMethod;
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
}

