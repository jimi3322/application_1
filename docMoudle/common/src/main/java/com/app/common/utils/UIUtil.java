package com.app.common.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 获取屏幕大小，dip变像素，像素变dip，同时封装了android 标准toast方法方便工程应用
 *
 * @author user
 */
public enum UIUtil {

    INSTANCE;
    private Application mApp = null;
    private DisplayMetrics mDisplayMetrics;

    private static final Handler handler = new Handler(Looper.getMainLooper());

    UIUtil() {
    }

    public void init(Application app) {
        mApp = app;
        mDisplayMetrics = new DisplayMetrics();
        WindowManager mWindowManager = ((WindowManager) mApp
                .getSystemService(Context.WINDOW_SERVICE));
        mWindowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
    }

    public DisplayMetrics getDisplayMetrics() {
        return mDisplayMetrics;
    }

    public String makeTimeString(long milliSecs) {

        int second = (int) milliSecs / 1000;

        return makeTimeString(second);
    }

    /**
     * 鐢熸垚瑕佹樉�?��殑鏃堕棿瀛楃锟�?
     *
     * @return
     */

    public String makeTimeString(int second) {
        StringBuffer sb = null;
        sb = new StringBuffer();
        int m = second / 60;
        sb.append(m < 10 ? "0" : "").append(m);
        sb.append(":");
        int s = second % 60;
        sb.append(s < 10 ? "0" : "").append(s);
        return sb.toString();
    }

    /**
     * 鏃堕棿鐨勮浆锟�
     *
     * @param time
     * @return
     */
    public String toTime(int time) {

        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    /**
     * 灏嗛渶瑕佺殑瀛椾綋澶у皬杞�?��涓哄疄闄呮樉�?��ぇ锟�?
     *
     * @param size
     * @return
     */
    public float getTextSizeAjustDensity(float size) {
        if (size <= 0) {
            size = 15;
        }
        float realSize = (float) (size * (getDensity() - 0.1));
        return realSize;
    }

    /**
     * 获取手机密度:low, mid, high, x
     */
    public float getDensity() {
        return mDisplayMetrics.density;
    }

    /**
     * 获取屏幕宽度
     */
    public int getmScreenWidth() {
        return mDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public int getmScreenHeight() {
        return mDisplayMetrics.heightPixels;
    }

    /**
     * dip变像px	 *
     *
     * @param dpValue
     * @return
     */
    public int DipToPixels(float dpValue) {
        final float scale = getDensity();
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 像素变dip
     *
     * @param pxValue
     * @return
     */
    public int PixelsToDip(float pxValue) {
        final float scale = getDensity();
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * android 标准提示
     *
     * @param id 文字id
     */
    public static void showToast(int id, Application context) {
        String mess = context.getResources().getString(id);
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
    }

    public void showToastNoneUI(final String mess) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                showToast(mess);
            }
        });
    }

    /**
     * 显示字符串
     *
     * @param mess
     */
    public void showToast(String mess) {
        if (TextUtils.isEmpty(mess)) {
            return;
        }
        Toast.makeText(mApp, mess, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int strId) {
        String mess = mApp.getResources().getString(strId);
        showToast(mess);
    }
    /**
     * 显示字符串
     *
     * @param mess
     */
    public void showToastLongTime(String mess) {
        if (TextUtils.isEmpty(mess)) {
            return;
        }
        Toast.makeText(mApp, mess, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示字符串
     *
     * @param mess
     */
    public static void showToast(String mess, Application context) {
        if (TextUtils.isEmpty(mess)) {
            return;
        }
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
    }
}
