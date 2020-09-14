package com.app.common.cookie;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;

import com.app.baselib.http.OkHttpUtils;
import com.app.common.domain.Constant;
import com.app.common.utils.HttpUtil;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class CookieManagerModule extends ReactContextBaseJavaModule {
    private static final String REACT_CLASS = "CookieManager";

    private static final String OPTIONS_NAME = "name";
    private static final String OPTIONS_VALUE = "value";
    private static final String OPTIONS_DOMAIN = "domain";
    private static final String OPTIONS_EXPIRATION = "expiration";

    public CookieManagerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void setLoginCookie(String url) {
        String name;
        String value;
        String domain;
        String expiration;
        if(!TextUtils.isEmpty(CookieManager.getInstance().getCookie(url))){
            return;
        }

        WritableArray array = getCookie( HttpUtil.getServiceAddress(Constant.LOGIN));
        if(array!=null && array.size()>0){
            for(int i = 0;i<array.size();i++){
                name = null;
                value = null;
                expiration =null;
                ReadableMap options = array.getMap(i);
                if (options.hasKey(OPTIONS_NAME)) {
                    name = options.getString(OPTIONS_NAME);
                }
                if (options.hasKey(OPTIONS_VALUE)) {
                    value = options.getString(OPTIONS_VALUE);
                }
                if (options.hasKey(OPTIONS_EXPIRATION)) {
                    expiration = options.getString(OPTIONS_EXPIRATION);
                }
                domain = HttpUrl.parse(url).host();
                CookieManager.getInstance().setCookie(url, name + "=" + value + ";"
                        + OPTIONS_EXPIRATION + "=" + expiration+";"
                        + OPTIONS_DOMAIN + "=" + domain);
            }
        }

    }

    /**
     * Gets the cookies for the given URL.
     *
     * @return value the cookies as a string, using the format of the 'Cookie'
     * HTTP request header
     */
    public WritableArray getCookie(String url) {
        try{
            HashMap<String, List<Cookie>> cookies = OkHttpUtils.getInstance().getCookie();
          HttpUrl httpUrl = HttpUrl.parse(url);
            if(cookies==null ||!cookies.containsKey(httpUrl.host())){
                return null;
            }
            List<Cookie> loginCookie = cookies.get(httpUrl.host());
            WritableArray array = Arguments.createArray();
            for(int i = 0;i<loginCookie.size();i++){
                Cookie cookie = loginCookie.get(i);
                WritableMap map = Arguments.createMap();
                map.putString(OPTIONS_NAME, cookie.name());
                map.putString(OPTIONS_VALUE, cookie.value());
                map.putString(OPTIONS_DOMAIN, cookie.domain());
                array.pushMap(map);
            }
            return array;
        }catch (Exception e){
            //errorBack.invoke(e.getMessage());
            return null;
        }
    }

    @ReactMethod
    public void removeAllCookies() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                }
            });
        } else {
            CookieManager.getInstance().removeAllCookie();
        }
    }
}