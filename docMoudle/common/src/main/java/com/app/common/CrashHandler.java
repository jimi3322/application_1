package com.app.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import com.app.baselib.BaseApplication;
import com.app.baselib.http.OkHttpUtils;
import com.app.common.domain.Constant;
import com.app.common.utils.HttpUtil;
import com.app.common.utils.StringUtil;
import com.app.wayee.common.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by WUJINGWEI on 2018/3/12.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final int DELAY_KILL_TIME = 10000;//延迟杀死进程等待毫秒数

    private Context mContext;

    private static CrashHandler crashHandler = new CrashHandler();

    private CrashHandler() {
    }

    public static CrashHandler getCrashHandlerInstance() {
        return crashHandler;
    }

     /**
     * 初始化数据
     *
     * @param context 上下文对象
     */
    public void initData(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    /**
     * 未捕获异常处理
     *
     * @param t 当前线程
     * @param e 未捕获异常对象
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        uploadExceptionToServer(e);
        SystemClock.sleep(DELAY_KILL_TIME);//延迟设定时间后杀死进程
        BaseApplication.mBaseApplication.removeAllActivity();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    /**
     *  收集崩溃时的设备信息
     * 并上传异常信息到服务器
     *
     * @param e 未捕获异常对象
     */
    private void uploadExceptionToServer(final Throwable e) {
        String info = "";
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(current));
        info+= Constant.APP_NAME + getAppName(mContext);
        info += Constant.CRASH_TIME + Constant.ID_COLON_CN + time + Constant.ID_ENTER;
        info += mContext.getResources().getString(R.string.app_version) + Constant.ID_V + StringUtil.getVersionName(mContext) + Constant.ID_ENTER; //程序版本号
        info += Constant.ANDROID_VERSION + Constant.ID_COLON_CN + Build.VERSION.RELEASE + Constant.ID_ENTER;
        info += Constant.ANDROID_API_VERSION + Constant.ID_COLON_CN + Build.VERSION.SDK_INT + Constant.ID_ENTER;
        info += Constant.PHONE_MANUFACTURER + Constant.ID_COLON_CN + Build.MANUFACTURER + Constant.ID_ENTER;
        info += Constant.PHONE_TYPE + Constant.ID_COLON_CN + Build.MODEL + Constant.ID_ENTER;
        info += Constant.STACK_TRACE_INFO + Constant.ID_COLON_CN + StringUtil.getStackTrace(e);

        OkHttpUtils.getInstance().post(HttpUtil.getServiceAddress(Constant.WRITE_LOG),info,null);
    }


    /**
     * 获取App名称
     *
     * @param context 上下文对象
     * @return App名称
     */
    private String getAppName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), 0);
            return pm.getApplicationLabel(appInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

}
