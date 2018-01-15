package com.luuu.seven.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/02
 *     desc   :
 *     version:
 */
class CollectHelp : SQLiteOpenHelper {

    companion object {
        private val VERSION = 1
        private val NAME = "collect.db"
        val TABLE_NAME = "collect"
    }

    constructor(context: Context) : super(context, NAME, null, VERSION)

    override fun onCreate(db: SQLiteDatabase) {
        val sql = "create table if not exists " + TABLE_NAME + " (_id integer primary key autoincrement," +
                "comicId integer,comicTitle text,comicAuthors text,comicCover text," +
                "collectTime integer)"
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}