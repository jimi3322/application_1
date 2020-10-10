package com.example.lib.http;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.example.lib.BaseApplication;
import com.example.lib.http.request.ProgressRequestBody;
import com.example.lib.http.response.GsonResponseHandler;
import com.example.lib.http.response.IResponseHandler;
import com.example.lib.utils.LLog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {
    private OkHttpClient client;
    private static OkHttpUtils instance;
    private HashMap<String, List<Cookie>> cookieStore;

    public OkHttpUtils() {
        cookieStore = new HashMap<>();
        client = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                if(url.toString().contains("Login")){
                    cookieStore.put(url.host(), cookies);
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());

                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }).connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                .build();
    }

    /**
     * 返回cookie
     * @return
     */
    public HashMap<String, List<Cookie>> getCookie(){
        return cookieStore;
    }

    /**
     * 给对应的url设置cookie
     */
    public void  setCookie(URL url, List<Cookie> cookie){
        List<Cookie> cookies = cookieStore.get(url.getHost());
        try{
            if(cookies == null){
                cookieStore.put(url.getHost(), cookie);
            }else{
                cookies.addAll(cookie);
            }
        }catch (Exception e){

        }

    }

    /**
     * 清除cookie
     * @return
     */
    public void removeCookie(){
        cookieStore.clear();
    }
    /**
     * 获取句柄
     * @return
     */
    public static OkHttpUtils getInstance() {
        if(instance == null) {
            instance = new OkHttpUtils();
        }

        return instance;
    }
    /**
     * post 请求
     * @param url url
     * @param params 参数
     * @param responseHandler 回调
     */
    public void post(final String url, final Map<String, String> params, final IResponseHandler responseHandler) {
        post(null, url, params, responseHandler);
    }

    /**
     * post 请求
     * @param context 发起请求的context
     * @param url url
     * @param params 参数
     * @param responseHandler 回调
     */
    public void post(Context context, final String url, final Map<String, String> params, final IResponseHandler responseHandler) {
        //post builder 参数
        FormBody.Builder builder = new FormBody.Builder();
        if(params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                Log.i("zyh", "post: key = " + entry.getKey() + " value = " + entry.getValue());
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        Request request;

        //发起request
        if(context == null) {
            request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .tag(context)
                    .build();
        }


        client.newCall(request).enqueue(new MyCallback(new Handler(), responseHandler));
    }

    /**
     * 上传json串
     * @param url
     * @param json
     * @param responseHandler
     */
    public void post(final String url, String json, final IResponseHandler responseHandler){
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        Request request = new Request.Builder()
                .url(url)//请求的url
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new MyCallback(new Handler(), responseHandler));
    }

    /**
     * get 请求
     * @param url url
     * @param params 参数
     * @param responseHandler 回调
     */
    public void get(final String url, final Map<String, String> params, final IResponseHandler responseHandler) {
        get(null, url, params, responseHandler);
    }

    /**
     * get 请求
     * @param context 发起请求的context
     * @param url url
     * @param params 参数
     * @param responseHandler 回调
     */
    public void get(Context context, final String url, final Map<String, String> params, final IResponseHandler responseHandler) {
        //拼接url
        String get_url = url;
        if(params != null && params.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if(i++ == 0) {
                    get_url = get_url + "?" + entry.getKey() + "=" + entry.getValue();
                } else {
                    get_url = get_url + "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }

        Request request;

        //发起request
        if(context == null) {
            request = new Request.Builder()
                    .url(url)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .tag(context)
                    .build();
        }

        client.newCall(request).enqueue(new MyCallback(new Handler(), responseHandler));
    }

    /**
     * 上传文件
     * @param url url
     * @param files 上传的文件files
     * @param responseHandler 回调
     */
    public void upload(String url, Map<String, File> files, final IResponseHandler responseHandler) {
        upload(null, url, null, files, responseHandler);
    }

    /**
     * 上传文件
     * @param url url
     * @param params 参数
     * @param files 上传的文件files
     * @param responseHandler 回调
     */
    public void upload(String url, Map<String, String> params, Map<String, File> files, final IResponseHandler responseHandler) {
        upload(null, url, params, files, responseHandler);
    }

    /**
     * 上传文件
     * @param context 发起请求的context
     * @param url url
     * @param files 上传的文件files
     * @param responseHandler 回调
     */
    public void upload(Context context, String url, Map<String, File> files, final IResponseHandler responseHandler) {
        upload(context, url, null, files, responseHandler);
    }

    /**
     * 上传文件
     * @param context 发起请求的context
     * @param url url
     * @param params 参数
     * @param files 上传的文件files
     * @param responseHandler 回调
     */
    public void upload(Context context, String url, Map<String, String> params, Map<String, File> files, final IResponseHandler responseHandler) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //添加参数
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                multipartBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }

        //添加上传文件
        if (files != null && !files.isEmpty()) {
            RequestBody fileBody;
            for (String key : files.keySet()) {
                File file = files.get(key);
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                multipartBuilder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + key + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        Request request;
        if(context == null) {
            request = new Request.Builder()
                    .url(url)
                    .post(new ProgressRequestBody(multipartBuilder.build(),responseHandler))
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(new ProgressRequestBody(multipartBuilder.build(),responseHandler))
                    .tag(context)
                    .build();
        }

        client.newCall(request).enqueue(new MyCallback(new Handler(), responseHandler));
    }
    //获取mime type
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    //callback
    private class MyCallback implements Callback {

        private Handler mHandler;
        private IResponseHandler mResponseHandler;

        public MyCallback(Handler handler, IResponseHandler responseHandler) {
            mHandler = handler;
            mResponseHandler = responseHandler;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseHandler.onFailure(0, e.toString());
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            if(response.isSuccessful()) {
                final String responseBody = response.body().string();
                LLog.d("zyh", "onResponse: = " + responseBody);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            int result = jsonObject.getInt("result");
                            switch (result){
                                case 1:{
                                    if(mResponseHandler == null){
                                        return;
                                    }
                                    Type type = ((GsonResponseHandler)mResponseHandler).getType();
                                    if(type == String.class){
                                        ((GsonResponseHandler)mResponseHandler).onSuccess(result, responseBody);
                                    }else if(!"[]".equals(responseBody) && !"{}".equals(responseBody)){
                                        if(type == JSONArray.class) {
                                            ((GsonResponseHandler)mResponseHandler).onSuccess(result, new JSONArray(responseBody));
                                        }else if(type == JSONObject.class) {
                                            ((GsonResponseHandler)mResponseHandler).onSuccess(result, new JSONObject(responseBody));
                                        }else {
                                            ((GsonResponseHandler)mResponseHandler).onSuccess(result,
                                                    gson.fromJson(responseBody,type));
                                        }
                                    }
                                    break;
                                }
                                case 0://未登录
                                case 2://登录超时
//                                    Intent intent = new Intent();
//                                    intent.setClassName(BaseApplication.mBaseApplication.getPackageName(),"com.app.common.activity.LoginActivity");
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    BaseApplication.mBaseApplication.removeAllActivity();
//                                    BaseApplication.mBaseApplication.startActivity(intent);

                                    //发个重新登录的广播
                                    Intent loginInten = new Intent("reLogin");
                                    BaseApplication.mBaseApplication.sendBroadcast(loginInten);
                                    break;
                                default:
                                    ((GsonResponseHandler)mResponseHandler).onSuccess(result,jsonObject.getString("data"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            int result = 0;
                            try {
                                JSONObject jsonObject = new JSONObject(responseBody);
                                result = jsonObject.getInt("result");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            } finally {
                                ((GsonResponseHandler)mResponseHandler).onSuccess(result, null);
                            }

                        }

                    }
                });
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mResponseHandler.onFailure(response.code(),  response.message());
                        mHandler.removeCallbacksAndMessages(null);
                    }
                });
            }
        }
    }
}
