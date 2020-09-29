package com.app.yun.banner

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.app.yun.R
import com.app.yun.base.BaseActivity
import kotlinx.android.synthetic.main.activity_search.*


class SPSearchActivity : BaseActivity() {
    private val PREFERENCE_NAME = "superservice_ly"
    private val SEARCH_HISTORY = "linya_history"

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }
    override fun initToolbar() {}

    override fun initData() {}

    override fun initView() {

        // 清空搜索历史
        tv_clear.setOnClickListener {
            clearSearchHistory()
            Toast.makeText(this@SPSearchActivity, "记录已清除", Toast.LENGTH_SHORT).show()
        }
        // 搜索框的键盘搜索键点击回调
        et_search.setOnKeyListener(object : View.OnKeyListener {// 输入完后按键盘上的搜索键
        override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() === KeyEvent.ACTION_DOWN) {// 修改回车键功能
                // 先隐藏键盘
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )
                saveSearchHistory(et_search.text.toString().trim())
                getSearchHistory()
                // TODO 根据输入的内容模糊查询商品，并跳转到另一个界面，由你自己去实现
                Toast.makeText(this@SPSearchActivity, "clicked!", Toast.LENGTH_SHORT).show()
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

            }
        })

        examplelistView?.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val textView = view.findViewById<View>(android.R.id.text1) as TextView
                val name = textView.getText().toString()
                et_search.setText(name)
                Toast.makeText(this@SPSearchActivity, name, Toast.LENGTH_SHORT).show()
                // TODO 获取到item上面的文字，根据该关键字跳转到另一个页面查询，由你自己去实现
            }
        })

        toback.setOnClickListener {
            finish()
        }

        // 第一次进入查询所有的历史记录
        getSearchHistory()
    }


    /**
     * 保存搜索记录
     */
    fun saveSearchHistory(inputText: String) {
        //初始化SharedPreferences（？？？？？？？？？放在外面会报空指针异常）
        val sp = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val longHistory = sp.getString(SEARCH_HISTORY, "")  //获取之前保存的历史记录
        val historyList = longHistory?.split(",")?.toMutableList()//逗号截取 保存在数组中
        val editor = sp.edit()
        if (TextUtils.isEmpty(inputText)) {
            return
        }
        if (historyList != null) {
            if (historyList.contains(inputText)){
                //添加重复元素选择忽略
            }else{
                historyList.add(0, inputText) //将新输入的文字添加集合的第0位也就是最前面(2.倒序)
                if (historyList.size > 8) {
                    historyList.removeAt(historyList.size - 1) //3.最多保存8条搜索记录 删除最早搜索的那一项
                }
                //逗号拼接
                val sb = StringBuilder()
                for (i in historyList.indices ) {
                    sb.append(historyList.get(i) + ",")
                }
                sb.delete(sb.length-1,sb.length)//去除最后一个逗号
                //保存到sp
                editor.putString(SEARCH_HISTORY, sb.toString())
                editor.commit()
            }

        } else {
            //之前未添加过
            editor.putString(SEARCH_HISTORY, "$inputText,")
            editor.commit()
        }
    }

    /**
     *  获取搜索记录
     */
    fun getSearchHistory() {
        val sp = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val longHistory = sp.getString(SEARCH_HISTORY, "")  //获取之前保存的历史记录
        val historyList = longHistory?.split(",")?.toMutableList()//逗号截取 保存在数组中
        //historyList?.removeAt(historyList.size)
//        val historyList = ArrayList(arrList)//split后长度为1有一个空串对象
        if (historyList?.size == 1 && historyList.isEmpty()) { //如果没有搜索记录，split之后第0位是个空串的情况下
            historyList.clear()  //清空集合
        }
        val adapter = historyList?.let {
            ArrayAdapter(this@SPSearchActivity, android.R.layout.simple_list_item_1,
                it
            )
        }
        examplelistView?.setAdapter(adapter)
        setListViewHeightBasedOnChildren(examplelistView)
        adapter?.notifyDataSetChanged()
    }

    /**
     *  清空搜索记录
     */
    fun clearSearchHistory() {
        val sp = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val longHistory = sp.getString(SEARCH_HISTORY, "")  //获取之前保存的历史记录
        val historyList = longHistory?.split(",")?.toMutableList()//逗号截取 保存在数组中
        val editor = sp.edit()
        historyList?.clear()
        val adapter = historyList?.let {
            ArrayAdapter(this@SPSearchActivity, android.R.layout.simple_list_item_1,
                it
            )
        }
        examplelistView?.setAdapter(adapter)
        setListViewHeightBasedOnChildren(examplelistView)
        adapter?.notifyDataSetChanged()
        //保存到sp
        editor.putString(SEARCH_HISTORY, null)
        editor.commit()
    }


    /**
     *  解决Android中ListView列表只显示一项数据的问题
     *  思路：获取每项item的高度，并相加，再加上分割线的高度，作为整个ListView的高度
     */
    fun setListViewHeightBasedOnChildren(listView: ListView) {
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
        listView.layoutParams = params
    }
}
