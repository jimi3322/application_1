package com.app.myapplication.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.myapplication.R
import com.app.myapplication.common.App
import com.app.myapplication.common.HttpUtil
import com.app.myapplication.service.MyIntentService
import com.app.myapplication.service.MyService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_contact.*
import okhttp3.*
import java.io.IOException
import kotlin.concurrent.thread


class ContactActivity : AppCompatActivity(), View.OnClickListener {
    /**
     *使用handler在子线程中异步更新UI
     */
    //开辟的子线程的数据交给handler处理
    val updateText = 1
    val handler = object : Handler(){
        override fun handleMessage(msg: Message) {
            //在这可以进行UI操作
            when(msg.what){
                updateText -> {
                    text1.text = "改变后的文字"
                }
            }
        }
    }
    override fun onClick(v : View?) {
        when(v ?.id){
            /**
             * 使用handler更新UI
             */
            R.id.handlerUI -> {
                //开辟线程
                thread {
                    val msg = Message()
                    msg.what  = updateText
                    handler.sendMessage(msg)
                }
            }
            /**
             * 开启和关闭前台服务按钮
             */
            R.id.startServicebtn -> {
                //开启服务
                val intent = Intent(this,MyService::class.java)
                startService(intent)
            }
            R.id.stopServicebtn -> {
                //关闭服务
                val intent = Intent(this,MyService::class.java)
                stopService(intent)
            }
            /**
             * 开启和关闭Intent服务按钮:会自动停止的服务
             */
            R.id.startIntentServicebtn -> {
                //打印主线程的ID
                Log.d("myIntentService","Thresd id is ${Thread.currentThread().name}")
                val intent = Intent(this,MyIntentService::class.java)
                startService(intent)
            }
            /**
             * 网络请求OKHttp
             */
            R.id.sendRequestBtn -> {
                sendRequestWithOKHttp()
            }
            /**
             * 使用工具类HTTPUtils网络请求并解析数据
             */
            R.id.httpUtilBtn -> {
                sendRequestWithHttpUtil()
            }
        }
    }

    private val contactList = ArrayList<String>()
    private lateinit var adpter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.app.myapplication.R.layout.activity_contact)

        adpter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactList)
        contactView.adapter = adpter
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS),1)
        }else{
            readContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readContacts()
                }else{
                    Toast.makeText(this,"你已拒绝联系人读取的请求",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private  fun readContacts(){
        //查询联系人数据
        contentResolver.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null)?.apply {
            while (moveToNext()){
                //获取联系人姓名
                val displayNames =  getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number =  getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contactList.add("$displayNames\n$number")//添加到listview的数据源里
            }
            adpter.notifyDataSetChanged()//数据更新就刷新listview
            close()//关闭Cursor对象
        }
    }


    private fun sendRequestWithOKHttp(){
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://192.168.200.203/get_data.json")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData != null){
                    showResponse(responseData)
                    parseJSONWitnGSON(responseData)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    private fun showResponse(response:String){
        runOnUiThread{
            //UI操作
            responseText.text = response
        }
    }
    private fun parseJSONWitnGSON(jsonData:String){
        val gson = Gson()
        val typeOf = object : TypeToken<List<App>>(){}.type
        val appList = gson.fromJson<List<App>>(jsonData,typeOf)
        val sentence = StringBuilder().apply{
            for (app in appList){
                val item = "id is ${app.id},name is ${app.name},version is ${app.version}"
                append(item)
            }
            toString()
        }
        runOnUiThread{
            //UI操作
            responseGson.text = sentence
        }

    }
    private fun sendRequestWithHttpUtil(){
        val address = "http://192.168.200.203/get_data.json"
        HttpUtil.sendOKHttpRequest(address, object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(this@ContactActivity,"请检查网络，连接失败",Toast.LENGTH_SHORT)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (responseData != null){
                    parseJSONWitnGSON(responseData)
                }
            }
        })
    }
}



