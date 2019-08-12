package com.luuu.seven.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.luuu.seven.ComicApplication
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.util.SchedulerHelper
import io.reactivex.Observable

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/03
 *     desc   :
 *     version:
 */
class ReadHistoryDao {
    private val ORDER_COLUMNS = arrayOf("comicId", "chapterId", "chapterTitle",
            "browsePosition", "comicCover", "comicTitle")
    var mHelp: ReadHistoryHelp? = null
    lateinit var db: SQLiteDatabase

    init {
        if (mHelp == null) createHelp()
    }

    companion object {
        fun get(): ReadHistoryDao {
            return Single.Instance
        }
    }

    private object Single {
        val Instance = ReadHistoryDao()
    }

    fun createHelp() {
        mHelp = ReadHistoryHelp(ComicApplication.mApp)
    }

    fun insertHistory(id: Int, chapterId: Int,
                      chapterTitle: String, browsePosition: Int,
                      comicCover: String, comicTitle: String): Observable<Boolean> {
        return Observable.defer{  Observable.just(addHistory(id, chapterId, chapterTitle, browsePosition,
                comicCover, comicTitle)) }
                .compose(SchedulerHelper.io_main())
    }

    fun isReadInChapter(id: Int): Observable<Boolean> {
        return Observable.defer{  Observable.just(isRead(id)) }
                .compose(SchedulerHelper.io_main())
    }

    fun updateReadHistory(id: Int, chapterId: Int,
                          chapterTitle: String, browsePosition: Int,
                          comicCover: String, comicTitle: String): Observable<Boolean> {
        return Observable.defer { Observable.just(updateHistory(id, chapterId, chapterTitle, browsePosition,
                comicCover, comicTitle)) }.compose(SchedulerHelper.io_main())
    }

    fun getReadHistory(): Observable<List<ReadHistoryBean>> {
        return Observable.defer { Observable.just(getAllHistory()) }.compose(SchedulerHelper.io_main())
    }

    fun queryByComicId(comicId: Int): Observable<List<ReadHistoryBean>?> {
        return Observable.defer { Observable.just(getHistoryById(comicId)) }.compose(SchedulerHelper.io_main())
    }

    private fun addHistory(comicId: Int, chapterId: Int, chapterTitle: String, browsePosition: Int,
                           comicCover: String, comicTitle: String): Boolean {
        try {
            db = mHelp!!.writableDatabase
            db.beginTransaction()
            val contentValues = ContentValues()
            contentValues.put("comicId", comicId)
            contentValues.put("chapterId", chapterId)
            contentValues.put("browsePosition", browsePosition)
            contentValues.put("chapterTitle", chapterTitle)
            contentValues.put("comicCover", comicCover)
            contentValues.put("comicTitle", comicTitle)
            db.insertOrThrow(ReadHistoryHelp.TABLE_NAME, null, contentValues)
            db.setTransactionSuccessful()
            return true
        } catch (e: Exception) {

        } finally {
            db.endTransaction()
            db.close()
        }
        return false
    }

    private fun updateHistory(comicId: Int, chapterId: Int, chapterTitle: String, browsePosition: Int,
                              comicCover: String, comicTitle: String): Boolean {
        try {
            db = mHelp!!.writableDatabase
            db.beginTransaction()
            val contentValues = ContentValues()
            contentValues.put("comicId", comicId)
            contentValues.put("chapterId", chapterId)
            contentValues.put("browsePosition", browsePosition)
            contentValues.put("chapterTitle", chapterTitle)
            contentValues.put("comicCover", comicCover)
            contentValues.put("comicTitle", comicTitle)
            val whereClause = "comicId = ?"
            val whereArgs = arrayOf(comicId.toString())
            db.update(ReadHistoryHelp.TABLE_NAME, contentValues, whereClause, whereArgs)
            db.setTransactionSuccessful()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
            db.close()
        }
        return false
    }

    private fun isRead(comicId: Int): Boolean {
        var cursor: Cursor? = null

        try {
            db = mHelp!!.readableDatabase
            cursor = db.query(ReadHistoryHelp.TABLE_NAME,
                    ORDER_COLUMNS,
                    "comicId = ?",
                    arrayOf(comicId.toString()), null, null, null)
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

    private fun getHistoryById(comicId: Int): List<ReadHistoryBean>? {
        var cursor: Cursor? = null
        val orderList = ArrayList<ReadHistoryBean>()
        try {
            db = mHelp!!.readableDatabase
            cursor = db.query(ReadHistoryHelp.TABLE_NAME,
                    ORDER_COLUMNS,
                    "comicId = ?",
                    arrayOf(comicId.toString()), null, null, null)
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


    private fun getAllHistory(): List<ReadHistoryBean> {
        var cursor: Cursor? = null
        val orderList = ArrayList<ReadHistoryBean>()
        try {
            db = mHelp!!.readableDatabase
            cursor = db.query(ReadHistoryHelp.TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null)
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

    private fun parseOrder(cursor: Cursor): ReadHistoryBean {
        val historyBean = ReadHistoryBean()
        historyBean.comicId = cursor.getInt(cursor.getColumnIndex("comicId"))
        historyBean.chapterId = cursor.getInt(cursor.getColumnIndex("chapterId"))
        historyBean.browsePosition = cursor.getInt(cursor.getColumnIndex("browsePosition"))
        historyBean.chapterTitle = cursor.getString(cursor.getColumnIndex("chapterTitle"))
        historyBean.comicCover = cursor.getString(cursor.getColumnIndex("comicCover"))
        historyBean.comicTitle = cursor.getString(cursor.getColumnIndex("comicTitle"))
        return historyBean
    }
}