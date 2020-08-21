package com.app.myapplication.ui.activity

import android.content.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.myapplication.R
import com.app.myapplication.common.ActivityCollector
import com.app.myapplication.common.Fruit
import com.app.myapplication.common.FruitAdapter
import com.app.myapplication.common.MyDatabaseHelper
import kotlinx.android.synthetic.main.activity_menu.*
import java.io.*

class MenuActivity : BaseActivity(), View.OnClickListener {
    lateinit var toastbroadcastReceiver: ToastBroadcastReceiver
    override fun onClick(v: View?) {
        when(v?.id){
           R.id.button1 -> {
               /**
                * 动态广播
                */
               val intentFilter = IntentFilter()
               intentFilter.addAction("com.app.myapplication.ui.activity.MY_BROADCAST")
               toastbroadcastReceiver = ToastBroadcastReceiver()
               registerReceiver(toastbroadcastReceiver,intentFilter)
           }
            R.id.delete -> {
                AlertDialog.Builder(this).apply {
                    setTitle("提示")
                    setMessage("点击删除")
//                    setCancelable(false)
                    setPositiveButton("ok"){
                            dialog ,witch ->
                    }
                    setNegativeButton("cancel"){dialog,witch ->

                    }
                }.show()
            }
            R.id.createDatabase -> {
                /**
                 * 建立数据库并查询
                 */
                val dbHelper = MyDatabaseHelper(this,"BookStore.db",1)//创建一个MyDatabaseHelper对象，指定数据库的名称
                val db = dbHelper.writableDatabase  //创建数据库
                //添加数据
                val value1 = ContentValues().apply {
                    put("name","q")
                    put("author","x")
                    put("pages",21)
                    put("price",15.8)
                }
                db.insert("Book",null,value1)
                //查询数据
                val cursor  = db.query("Book",null,null,null,null,null,null)
                if (cursor.moveToFirst()){   //将数据指针移至第一行开始遍历数据
                    do {
                        val name = cursor.getString(cursor.getColumnIndex("name"))
                        val author = cursor.getString(cursor.getColumnIndex("author"))
                        val pages = cursor.getString(cursor.getColumnIndex("pages"))
                        val price = cursor.getString(cursor.getColumnIndex("price"))
                        Toast.makeText(this,"Book表中的所有数据：$name,$author,$pages,$price",Toast.LENGTH_LONG).show()
                    }while (cursor.moveToNext())
                }
                cursor.close()
            }
            R.id.readContact -> {
                /**
                    读取联系人信息
                 */
                val intent = Intent(this, ContactActivity::class.java)
                startActivity(intent)
            }
            R.id.materialDesign -> {
                /**
                UI:MaterialDesign
                 */
                val intent = Intent(this, MaterialDesignActivity::class.java)
                startActivity(intent)
            }

           /* R.id.forceoffline -> {
                *//**
                 * 静态广播
                 *//*
                val intent = Intent("com.app.myapplication.common.MY_BROADCAST")
                intent.setPackage(packageName)
                sendBroadcast(intent)
            }*/
        }
    }

    private val fruitList = ArrayList<Fruit>()
    private fun initFruits(){
        repeat(4){
            fruitList.add(Fruit("苹果",R.drawable.login_selected))
            fruitList.add(Fruit("桃子",R.drawable.login_selected))

            //瀑布布局
            /*fruitList.add(Fruit(getRandomLengthString("苹果"),R.drawable.login_selected))
            fruitList.add(Fruit("桃子",R.drawable.login_selected))*/
        }
    }
    private fun getRandomLengthString(str:String):String{
        val n = (1..20).random()
        val builder = StringBuilder()
        repeat(8){
            builder.append(str)
        }
        return builder.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar?.hide()     //隐藏系统的标题栏
        /**
         * 数据存储在文件中
         */
        savefile()

        /**
         * 退出APP
         */
        backall.setOnClickListener{
            ActivityCollector.finishAll()
        }
        //button1.setOnClickListener(this)

        /**
         * 滚动控件
         */
        initFruits()
        //列表布局
        val layoutManager = LinearLayoutManager(this)
       // val layoutManager = StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL)      //此为瀑布流布局方式
        recyclerview.layoutManager = layoutManager
        val adapter = FruitAdapter(fruitList)
        recyclerview.adapter = adapter
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.add_item -> Toast.makeText(this,"菜单添加", Toast.LENGTH_SHORT).show()
            R.id.remove_item -> Toast.makeText(this,"菜单移除", Toast.LENGTH_SHORT).show()
        }
        return true
    }
    /**
     * 广播接收类
     */
    inner class ToastBroadcastReceiver : BroadcastReceiver(){          //自己定义的广播继承了BroadcastReceiver
        override fun onReceive(context: Context, intent: Intent) {
            Toast.makeText(context,"receive broadcast",Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * 数据存储在文件中
     */
    private fun savefile(){
        val inputText = load()
        if (inputText.isNotEmpty()){
            saveData.setText(inputText)
            saveData.setSelection(inputText.length)
        }
    }
    private fun save(inputText:String){
        try {
            val output = openFileOutput("data", Context.MODE_PRIVATE)
            val write = BufferedWriter(OutputStreamWriter(output))
            write.use {
                it.write(inputText)
            }
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
    private fun load():String{
        val content = StringBuilder()
        try {
            val input = openFileInput("data")
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    content.append(it)
                }
            }
        }catch (e:IOException){
            e.printStackTrace()
        }
        return content.toString()
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(toastbroadcastReceiver)

        val inputText = saveData.text.toString()            //在返回活动时将控件saveData里的内容存储在名为data的文件中
        save(inputText)
    }
}
