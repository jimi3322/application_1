package com.app.myapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by wayee on 2019/5/22.
 */

public class BaseApplication extends Application {
    public static BaseApplication mBaseApplication;
    private static ArrayList<Activity> activities = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication = this;
    }
    public void addActivity(Activity activity){
        activities.add(activity);
    }

    public void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public void removeAllActivity(){
        if(activities.size()>0){
            for(int i = 0 ; i<activities.size();i++){
                activities.get(i).finish();
            }
        }
    }

}
