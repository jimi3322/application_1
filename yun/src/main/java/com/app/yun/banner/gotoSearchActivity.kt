package com.app.yun.banner

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.app.yun.R
import com.app.yun.base.BaseActivity
import kotlinx.android.synthetic.main.activity_search.*


class gotoSearchActivity : BaseActivity() {
    private val TAG = "gotoSearchActivity"
    private var helper = RecordSQLiteOpenHelper(this)
    private var db: SQLiteDatabase? = null
    private var listView: MyListView? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }
    override fun initToolbar() {}

    override fun initData() {}

    override fun initView() {

        // 清空搜索历史
        tv_clear.setOnClickListener {
            deleteData()
            queryData("")
            Toast.makeText(this@gotoSearchActivity, "记录已清除", Toast.LENGTH_SHORT).show()
        }
        // 搜索框的键盘搜索键点击回调
        et_search.setOnKeyListener(object : View.OnKeyListener {// 输入完后按键盘上的搜索键
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() === KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                    )
                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    val hasData = hasData(et_search.text.toString().trim())
                    if (!hasData) {
                        insertData(et_search.text.toString().trim())
                        queryData("")
                    }
                    // TODO 根据输入的内容模糊查询商品，并跳转到另一个界面，由你自己去实现
                    Toast.makeText(this@gotoSearchActivity, "clicked!", Toast.LENGTH_SHORT).show()

                }
                return false
            }
        })
        // 搜索框的文本变化实时监听
        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().trim().length === 0) {
                    tv_tip.text = "搜索历史"
                } else {
                    tv_tip.text = "搜索结果"
                }
                val tempName = et_search.text.toString()
                // 根据tempName去模糊查询数据库中有没有数据
                queryData(tempName)
            }
        })

        listView?.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val textView = view.findViewById<View>(android.R.id.text1) as TextView
                val name = textView.getText().toString()
                et_search.setText(name)
                Toast.makeText(this@gotoSearchActivity, name, Toast.LENGTH_SHORT).show()
                // TODO 获取到item上面的文字，根据该关键字跳转到另一个页面查询，由你自己去实现
            }
        })

        toback.setOnClickListener {
            finish()
        }
        /*// 插入数据，第一次进入有数据便于测试
        val date = Date()
        val time = date.time
        insertData("Leo$time")
*/
        // 第一次进入查询所有的历史记录
        queryData("")
    }



    /**
     * 插入数据
     */
    private fun insertData(tempName: String) {
        Log.i(TAG,"insertData:"+"insert into records(name) values('$tempName')")
        db = helper.writableDatabase
        db?.execSQL("insert into records(name) values('$tempName')")
        db?.close()
    }

    /**
     * 模糊查询数据
     */
    private fun queryData(tempName: String) {
        var cursor = helper.getReadableDatabase().rawQuery(
            "select id as _id,name from records where name like '%$tempName%' order by id desc ",
            null
        )
        // 创建adapter适配器对象
        var adapter = SimpleCursorAdapter(
            this, android.R.layout.simple_list_item_1, cursor, arrayOf("name"),
            intArrayOf(android.R.id.text1), CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )

        // 设置SimpleCursorAdapter适配器，展示数据库数据
        listView?.setAdapter(adapter)
        adapter.notifyDataSetChanged()

        // 设置ArrayAdapter + ListView适配器,并通过setListViewHeightBasedOnChildren方法解决只展示一条数据的问题
        /*val str_arr = arrayOf("22", "33", "叮叮当当")
        val adapter2 = ArrayAdapter(this@gotoSearchActivity, android.R.layout.simple_list_item_1, str_arr)
        example?.setAdapter(adapter2)
        setListViewHeightBasedOnChildren(example)
        adapter2.notifyDataSetChanged()*/

    }

    /**
     *  解决Android中ListView列表只显示一项数据的问题
     *  思路：获取每项item的高度，并相加，再加上分割线的高度，作为整个ListView的高度
     */
    fun setListViewHeightBasedOnChildren(listView: ListView) {
        // 获取ListView对应的Adapter
        val listAdapter = listView.adapter ?: return   //这是自己ListView的适配器

        var totalHeight = 0
        var i = 0
        val len = listAdapter.count
        while (i < len) { // listAdapter.getCount()返回数据项的数目
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(0, 0) // 计算子项View 的宽高
            totalHeight += listItem.measuredHeight // 统计所有子项的总高度
            i++
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.layoutParams = params
    }


    /**
     * 检查数据库中是否已经有该条记录
     */
    private fun hasData(tempName: String): Boolean {
        val cursor = helper.readableDatabase.rawQuery(
            "select id as _id,name from records where name =?", arrayOf(tempName)
        )
        //判断是否有下一个
        return cursor.moveToNext()
    }

    /**
     * 清空数据
     */
    private fun deleteData() {
        db = helper.writableDatabase
        db?.execSQL("delete from records")
        db?.close()
    }


}
