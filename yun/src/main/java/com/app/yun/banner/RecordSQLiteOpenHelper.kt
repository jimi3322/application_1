package com.app.yun.banner


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RecordSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, name, null, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table records(id integer primary key autoincrement,name varchar(200))")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {

        private val name = "temp_search.db"
        private val version = 1
    }


}
