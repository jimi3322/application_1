package com.app.water.common;

import android.app.Application;
import android.content.Context;


public class WaterApplication extends Application {
    private static Context context;

    public static WaterApplication mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        mApp = this;
//        SDKInitializer.initialize(mApp);
    }

    //获取应用上下文环境,全局Context调用WaterApplication.getContext();
    public static Context getContext() {
        return context;
    }
}
