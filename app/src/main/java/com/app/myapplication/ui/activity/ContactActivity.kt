package com.app.myapplication.ui.activity

import android.content.ContentResolver
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_contact.*
import android.provider.Settings
import com.app.myapplication.R


class ContactActivity : AppCompatActivity() {

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
}



