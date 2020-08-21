package com.app.myapplication.http;

/**
 * Created by WUJINGWEI on 2018/8/30.
 */

import android.webkit.CookieManager;

/**
 * Cookie帮助器
 */
public class CookieHelper {

    /**
     * 获取Cookie
     *
     * @return Cookie值
     */
    public static String getCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (cookieManager == null) return "";
        String cookie = cookieManager.getCookie("cookie");
        return cookie == null ? "" : cookie;
    }

    /**
     * 设置Cookie
     *
     * @param cookie Cookie值
     */
    public static void setCookie(String cookie) {
        CookieManager cookieManager = CookieManager.getInstance();
        if (cookieManager == null) return;
        cookieManager.setCookie("cookie", cookie);
    }
}
