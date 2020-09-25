package com.app.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.app.myapplication.base.LoginBaseActivity
import com.app.myapplication.ui.activity.MenuActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : LoginBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //1、初始化获取sharedPrefrs的方式（覆盖）以及存储文件名
        val pref = getSharedPreferences("predata",Context.MODE_PRIVATE)
        //2、向predata存入中存入上次是否有记住账号密码的记录,false是开始的默认值
        val remeberpassword = pref.getBoolean("remeberpassword",false)
        //3、得到pref里的是否记住密码remeberpassword进行判断
        if (remeberpassword){
            //记住密码则将账号密码自动填入
            val account = pref.getString("account","")
            val password = pref.getString("password","")
            Log.d("account",account+":"+password)
            accountEdit.setText(account)
            passwordEdit.setText(password)
            remeberCheck.isChecked = true
        }
        //4、点击登录按钮登录
        LoginButton.setOnClickListener{
            val account = accountEdit.text.toString()
            val password = passwordEdit.text.toString()
            Log.d("accountlogin",account+":"+password)
            if (true){
                //5、对账号密码的验证过程，此处省略
                //。。。。。。。。。
                val editor = pref.edit()
                //6、判断复选框是否选中
                if (remeberCheck.isChecked){
                    //7、复选框选中则将账号密码存入sharedprefs
                    editor.putBoolean("remeberpassword",true)
                    editor.putString("account",account)
                    editor.putString("password",password)
                }else{
                    //8、复选框未选中则将sharedprefs的账号密码清除
                    editor.clear()
                }
                //9、提交sharedprefs的账号密码
                editor.apply()                                              //提交

                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
//                finish()//销毁这个activity,不是返回上一层
            }else{
                Toast.makeText(this,"未登陆成功",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
