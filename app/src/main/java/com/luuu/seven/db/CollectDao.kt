package com.luuu.seven.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.luuu.seven.MyApplication
import com.luuu.seven.bean.CollectBean
import com.luuu.seven.util.SchedulerHelper
import io.reactivex.Observable


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/02
 *     desc   :
 *     version:
 */
class CollectDao private constructor() {
    private val ORDER_COLUMNS = arrayOf("comicId", "comicTitle", "comicAuthors", "comicCover", "collectTime")
    var mHelp: CollectHelp?= null
    lateinit var db: SQLiteDatabase

    init {
        if (mHelp == null) createHelp()
    }

    companion object {
        fun get(): CollectDao {
            return Single.Instance
        }
    }

    private object Single {
        val Instance = CollectDao()
    }

    fun createHelp() {
        mHelp = CollectHelp(MyApplication.sAppContext)
    }

    fun insert(id: Int, title: String, authors: String, img: String, time: Long): Observable<Boolean> {
        return Observable.defer{  Observable.just(addCollect(id,title,authors,img,time)) }
                .compose(SchedulerHelper.io_main())
    }

    fun queryById(id: Int): Observable<Boolean> {
        return Observable.defer { Observable.just(isCollect(id)) }.compose(SchedulerHelper.io_main())
    }

    fun cancelCollect(id: Int): Observable<Boolean> {
        return Observable.defer { Observable.just(deletCollect(id)) }.compose(SchedulerHelper.io_main())
    }

    fun getCollect(): Observable<List<CollectBean>> {
        return Observable.defer { Observable.just(getAllCollect()) }.compose(SchedulerHelper.io_main())
    }

    private fun addCollect(id: Int, title: String, authors: String, img: String, time: Long): Boolean {
        try {
            db = mHelp!!.writableDatabase
            db.beginTransaction()
            val contentValues = ContentValues()
            contentValues.put("comicId", id)
            contentValues.put("comicTitle", title)
            contentValues.put("comicAuthors", authors)
            contentValues.put("comicCover", img)
            contentValues.put("collectTime", time)
            db.insertOrThrow(CollectHelp.TABLE_NAME, null, contentValues)
            db.setTransactionSuccessful()
            return true
        } catch (e: Exception) {

        } finally {
            db.endTransaction()
            db.close()
        }
        return false
    }

    private fun isCollect(comicId: Int): Boolean {
        var cursor: Cursor? = null
        try {
            db = mHelp!!.readableDatabase
            cursor = db.query(CollectHelp.TABLE_NAME,
                    ORDER_COLUMNS,
                    "comicId = ?",
                    arrayOf(comicId.toString()),
                    null, null, null)
            if (cursor!!.count > 0) {
                return true
            }
        } catch (e: Exception) {

        } finally {
            cursor?.close()
            db.close()
        }
        return false
    }

    private fun deletCollect(comicId: Int): Boolean {
        try {
            db = mHelp!!.writableDatabase
            db.beginTransaction()
            db.delete(CollectHelp.TABLE_NAME, "comicId = ?", arrayOf(comicId.toString()))
            db.setTransactionSuccessful()
            return true
        } catch (e: Exception) {

        } finally {
            db.endTransaction()
            db.close()
        }
        return false
    }

    private fun getAllCollect(): List<CollectBean> {
        var cursor: Cursor? = null
        val orderList = ArrayList<CollectBean>()
        try {
            db = mHelp!!.readableDatabase
            cursor = db.query(CollectHelp.TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null)
            cursor?.let {
                if (it.count > 0) {
                    while (it.moveToNext()) {
                        orderList.add(parseOrder(it))
                    }
                    return orderList
                }
            }

        } catch (e: Exception) {

        } finally {
            cursor?.close()
            db.close()
        }
        return orderList
    }

    private fun parseOrder(cursor: Cursor): CollectBean {
        val collect = CollectBean()
        collect.comicId = cursor.getInt(cursor.getColumnIndex("comicId"))
        collect.comicTitle = cursor.getString(cursor.getColumnIndex("comicTitle"))
        collect.comicAuthors = cursor.getString(cursor.getColumnIndex("comicAuthors"))
        collect.comicCover = cursor.getString(cursor.getColumnIndex("comicCover"))
        collect.collectTime = cursor.getLong(cursor.getColumnIndex("collectTime"))
        return collect
    }
}