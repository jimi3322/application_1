package com.app.water.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.app.water.MainActivity;
import com.app.water.R;
import com.example.lib.domain.Constant;
import com.example.lib.domain.LoginData;
import com.example.lib.http.OkHttpUtils;
import com.example.lib.http.response.GsonResponseHandler;
import com.example.lib.utils.CommonUtil;
import com.example.lib.utils.HttpUtil;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {


    private void LoginSuccessHandle() {
        Log.i("test", "LoginSuccessHandle: ");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartValidateLoginThread();
            }
        });
    }



    /**
     * 启动验证登录线程，Android 3.0以后只允许异步访问网络
     */
    private void StartValidateLoginThread() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constant.PARAM_USERID,"zhangyuanhao");
        params.put(Constant.PARAM_PASSWORD,"123456");
        try {
            params.put(Constant.PARAM_PASSWORD, CommonUtil.getMD5Str("123456"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkHttpUtils.getInstance().post(LoginActivity.this, HttpUtil.getServiceAddress(Constant.LOGIN),params , new GsonResponseHandler<LoginData>() {
            @Override
            public void onSuccess(int statusCode, LoginData data) {
                if(statusCode == 1){
                    Log.i("test", "onSuccess: 登录成功");
                    LoginSuccessHandle();
                }else{
                    Log.i("test", "onSuccess: 登录失败");
                }
            }
            @Override
            public void onFailure(int statusCode, String error_msg) {
                Log.i("test", "onSuccess: 网络失败");
            }
        });
    }


}