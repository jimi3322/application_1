package com.app.yun

import com.app.yun.base.BaseActivity
import com.app.yun.bean.User
import greendao.GreenDaoUtils
import kotlinx.android.synthetic.main.activity_greendao.*

class GreenDaoActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_greendao
    }

    override fun initView() {
        //2、GreenDao数据库操作方法
        //(1)删除所有数据
        GreenDaoUtils.getmy(this)?.getdelet_all()
        //(2)插入数据
        GreenDaoUtils.getmy(this)?.getinsert(User(1,"xx","111"))
        //(3)查询数据
        var listUser =  GreenDaoUtils.getmy(this)?.getquest_true("xx")

        datacontent.text = "id:${listUser?.get(0)?.id}   name:${listUser?.get(0)?.name}   password:${listUser?.get(0)?.password}"

    }

    override fun initToolbar() {
    }

    override fun initData() {
    }
}