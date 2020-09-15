package com.app.myapplication.utils;

import android.app.Application;

//import com.baidu.mapapi.SDKInitializer;

public class AirApplication extends Application {

    public static AirApplication mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
//        SDKInitializer.initialize(mApp);
    }
}
