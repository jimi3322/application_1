package com.app.yun

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import com.app.myapplication.common.LogUtil.d
import com.app.myapplication.common.domain.Constant
import com.app.myapplication.common.domain.LoginData
import com.app.myapplication.common.showToast
import com.app.myapplication.common.utils.CommonUtil
import com.app.myapplication.common.utils.http.CookieHelper
import com.app.myapplication.common.utils.http.GsonResponseHandler
import com.app.myapplication.common.utils.http.HttpUtil
import com.app.myapplication.common.utils.http.OkHttpUtils
import com.app.myapplication.common.utils.ui.UIUtil
import com.app.myapplication.ui.activity.BaseActivity
import com.app.myapplication.ui.activity.MenuActivity
import com.app.yun.ui.MActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val pref = getSharedPreferences("pfsdata", Context.MODE_PRIVATE)
        val remeberpassword = pref.getBoolean("remeberpassword",false)
        if (remeberpassword){
            val account = pref.getString("account","")
            val password = pref.getString("password","")
            d("account",account+":"+password)
            accountEdit.setText(account)
            passwordEdit.setText(password)
            remeberCheck.isChecked = true
        }

        LoginButton.setOnClickListener{
            val account = accountEdit.text.toString()
            val password = passwordEdit.text.toString()
            val hashMap = hashMapOf("userID" to account,"password" to CommonUtil.getMD5Str(password))
            d("accountlogin",account+":"+password)
            OkHttpUtils.getInstance().post(
                this@LoginActivity,
                HttpUtil.getServiceAddress(Constant.LOGIN),
                hashMap,
                object : GsonResponseHandler<LoginData>() {
                override fun onSuccess(statusCode: Int, data: LoginData?) {
                        if (statusCode == 1) {
                            if (data != null) {
                                CookieHelper.setCookie(data.data.JSESSIONID)
                            }
                            val editor = pref.edit()
                            if (remeberCheck.isChecked){
                                editor.putBoolean("remeberpassword",true)
                                editor.putString("account",account)
                                editor.putString("password",password)
                            }else{
                                editor.clear()
                            }
                            editor.apply()                                              //提交
                            val intent = Intent(this@LoginActivity, MActivity::class.java)
                            startActivity(intent)
                        } else {
//                            "未登陆成功".showToast(this@LoginActivity)
                            Toast.makeText(this@LoginActivity,"未登陆成功",Toast.LENGTH_SHORT).show()
                        }
                       // dismissLoadingView()
                    }

                override fun onFailure(statusCode: Int, error_msg: String) {
                   // dismissLoadingView()
                    Toast.makeText(this@LoginActivity,"网络问题",Toast.LENGTH_SHORT).show()
                   // UIUtil.INSTANCE.showToast("网络问题")//login_fail_net
                }
                })

        }
    }

}
