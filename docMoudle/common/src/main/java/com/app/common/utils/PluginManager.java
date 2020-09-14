package com.app.common.utils;

import android.content.Context;
import android.util.Log;

import com.app.baselib.BaseApplication;
import com.app.wayee.common.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by jtt on 2019/5/9.
 */

public class PluginManager {
    private static PluginManager ourInstance = new PluginManager();
    private Context mContext;
    private DexClassLoader pluginDexClassLoader;

    public static PluginManager getInstance() {
        return ourInstance;
    }

    private PluginManager() {
        mContext = BaseApplication.mBaseApplication;
    }

    //插件注入
    public boolean inject(String libPath) {
        boolean hasBaseDexClassLoader = true;
        try {
            Class.forName("dalvik.system.BaseDexClassLoader");
        } catch (ClassNotFoundException e) {
            hasBaseDexClassLoader = false;
        }
        if (hasBaseDexClassLoader) {
            PathClassLoader pathClassLoader = (PathClassLoader)mContext.getClassLoader();
            try {
                Object dexElements = combineArray(getDexElements(getPathList(pathClassLoader)),
                        getDexElements(getPathList( PluginManager.getInstance().loadApk(libPath))));
                Object pathList = getPathList(pathClassLoader);
                setField(pathList, pathList.getClass(), "dexElements", dexElements);
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 通过反射获取pathList对象
     * @param obj
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getPathList(Object obj) throws ClassNotFoundException, NoSuchFieldException,IllegalAccessException {
        return getField(obj, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }
    private static Object getField(Object obj, Class cls, String str) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(obj);
    }

    /**
     * 通过反射获得dexElements对象
     * @param obj
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getDexElements(Object obj) throws NoSuchFieldException, IllegalAccessException {
        return getField(obj, obj.getClass(), "dexElements");
    }

    /**
     * 将dex插入到数组中，插件的dex要放在前面
     * @param obj
     * @param obj2
     * @return
     */
    private static Object combineArray(Object obj, Object obj2) {
        Class componentType = obj2.getClass().getComponentType();
        int length = Array.getLength(obj2);
        int length2 = Array.getLength(obj) + length;
        Object newInstance = Array.newInstance(componentType, length2);
        for (int i = 0; i < length2; i++) {
            if (i < length) {
                Array.set(newInstance, i, Array.get(obj2, i));
            } else {
                Array.set(newInstance, i, Array.get(obj, i - length));
            }
        }
        return newInstance;
    }
    private static void setField(Object obj, Class cls, String str, Object obj2) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        declaredField.set(obj, obj2);
    }

    /**
     * 创建DexClassLoader加载dex文件中的类
     * @param dexName
     * @return
     */
    public DexClassLoader loadApk(String dexName) {
        String dexPath;
        File cacheFile = FileUtil.getFileDir(mContext);
        dexPath = cacheFile.getAbsolutePath() + File.separator + dexName;
        pluginDexClassLoader = new DexClassLoader(dexPath,
                mContext.getDir("dex", Context.MODE_PRIVATE).getAbsolutePath(),
                null,
                mContext.getClassLoader());
        return pluginDexClassLoader;
    }
}
