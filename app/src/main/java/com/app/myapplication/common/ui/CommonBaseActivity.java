package com.app.myapplication.common.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.app.myapplication.BaseApplication;
import com.app.myapplication.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommonBaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    protected String mPluginName;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 展示loading窗
     */
    protected void showLoadingView(String msg){
        if(TextUtils.isEmpty(msg)){
            msg = getResources().getString(R.string.request_data_in_process);
        }
        if(progressDialog == null){
            progressDialog = ProgressDialog.show(this, "", msg, true);
            progressDialog.setCancelable(false);
        }else{
            progressDialog.setMessage(msg);
            progressDialog.show();
        }

    }

    /**
     * 展示3s的loading窗
     * */
    protected void showLoadingView(String msg, long duration){
        Runnable runner = new Runnable() {
            @Override
            public void run() {
                dismissLoadingView();
            }
        };
        //新建调度任务
        executor.schedule(runner, duration, TimeUnit.MILLISECONDS);
        showLoadingView(msg);
    }

    /**
     * 自动关闭的loadingView
     * */
    protected void showAutoCloseLoadingView(String msg){
        showLoadingView(msg,3000);
    }


    /**
     * 隱藏loading窗
     */
    protected void dismissLoadingView(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        BaseApplication.mBaseApplication.removeActivity(this);
    }

}
