package com.app.myapplication;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by jiangtingting on 2019/6/18.
 */
public class WySharePCache {
    private static final String SHARENAME = "wyCloudApp";
    private static Application mApp = BaseApplication.mBaseApplication;
    public static void init(Application context){
        mApp = context;
    }
    /**
     * 删除缓存中的数据
     *
     * @param key
     * @return
     */
    public static boolean removeShareCach(String key) {
        SharedPreferences mySharedPreferences = mApp.getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.remove(key);
        return editor.commit();
    }
    /**
     * 往sharepreference里面存储字符串数据
     *
     * @param key
     *            存储的键名
     * @param content
     *            存储的内容
     * @return
     */
    public static boolean saveStringCach(String key, String content) {
        SharedPreferences mySharedPreferences = mApp.getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(key, content);
        return editor.commit();
    }

    /**
     * 从sharepreference里面取出字符串数据
     *
     * @param key
     *            取数据键名
     * @return
     */
    public static String loadStringCach(String key) {
        SharedPreferences sharedPreferences = mApp.getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    /**
     * 从sharepreference里面取出字符串数据
     *
     * @param key
     *            取数据键名
     * @return
     */
    public static String loadStringCach(String key, String defaultValue) {
        SharedPreferences sharedPreferences = mApp.getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * 往sharepreference里面存储整型数据
     *
     * @param key
     *            存储的键名
     * @param content
     *            存储的内容
     * @return
     */
    public static boolean saveIntCach(String key, int content) {
        SharedPreferences mySharedPreferences = mApp.getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(key, content);
        return editor.commit();
    }

    /**
     * 从sharepreference里面取出整型数据
     *
     * @param key
     *            取数据键名
     * @return
     */
    public static int loadIntCach(String key) {
        SharedPreferences sharedPreferences = mApp.getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    /**
     * 从sharepreference里面取出整型数据
     *
     * @param key
     *            取数据键名
     * @return
     */
    public static int loadIntCach(String key, int defaultValue) {
        SharedPreferences sharedPreferences = mApp.getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * 往sharepreference里面存储Boolean数据
     *
     * @param key
     *            存储的键名
     * @param content
     *            存储的内容
     * @return
     */
    public static boolean saveBooleanCach(String key, Boolean content) {
        SharedPreferences mySharedPreferences = mApp.getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(key, content);
        return editor.commit();
    }

    /**
     * 从sharepreference里面取出Boolean数据
     *
     * @param key
     *            取数据键名
     * @return
     */
    public static Boolean loadBooleanCach(String key) {
        SharedPreferences sharedPreferences = mApp.getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     * 从sharepreference里面取出Boolean数据
     *
     * @param key
     *            取数据键名
     * @return
     */
    public static Boolean loadBooleanCach(String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = mApp.getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 往sharepreference里面存储float数据
     *
     * @param key
     *            存储的键名
     * @param content
     *            存储的内容
     * @return
     */
    public static boolean saveFloatCach(String key, Float content) {
        SharedPreferences mySharedPreferences = mApp
                .getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putFloat(key, content);
        return editor.commit();
    }

    /**
     * 从sharepreference里面取出float数据
     *
     * @param key
     *            取数据键名
     * @return
     */
    public static Float loadFloatCach(String key) {
        SharedPreferences sharedPreferences = mApp
                .getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, 0);
    }

    public static Float loadFloatCach(String key, float def) {
        SharedPreferences sharedPreferences = mApp
                .getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, def);
    }

    /**
     * 往sharepreference里面存储Long数据
     *
     * @param key
     *            存储的键名
     * @param content
     *            存储的内容
     * @return
     */
    public static boolean saveLongCach(String key, Long content) {
        SharedPreferences mySharedPreferences = mApp
                .getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putLong(key, content);
        return editor.commit();
    }

    /**
     * 从sharepreference里面取出Long数据
     *
     * @param key
     *            取数据键名
     * @return
     */
    public static Long loadLongCach(String key) {
        SharedPreferences sharedPreferences = mApp
                .getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }

    /**
     * 从sharepreference里面取出Long数据
     *
     * @param key
     *            取数据键名
     * @return
     */
    public static Long loadLongCach(String key, long def) {
        SharedPreferences sharedPreferences = mApp
                .getSharedPreferences(SHARENAME, Activity.MODE_PRIVATE);
        return sharedPreferences.getLong(key, def);
    }
}
