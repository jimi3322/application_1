package com.app.common.activity;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.baselib.BaseApplication;
import com.app.wayee.common.R;

public class CommonBaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    protected String mPluginName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
