package com.app.myapplication.common

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(val context: Context,name: String,version: Int): SQLiteOpenHelper(context,name,null,version){

    private  val createBook = "create table Book ("+
            "id integer primary key autoincrement,"+
            "author text,"+
            "price real,"+
            "pages integer,"+
            "name text,"+
            "category_id integer)"
    private val createCategory = "create table Category ("+
            "id integer primary key autoincrement,"+
            "category_name text,"+
            "category_code integer)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createBook)
        db.execSQL(createCategory)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,newVersion: Int) {
        if (oldVersion <= 1){    //此处旧的版本号（旧版本号是当前的版本号）为1，表示原先存在book表，当数据库升级为新版本2后，只需增加Category这一张表
            db.execSQL(createCategory)
        }
        if (oldVersion <= 2){   //当前版本号到2的时候，会执行之前book的数据添加操作
            db.execSQL("alter table Book add column category_id integer")
        }
    }


}