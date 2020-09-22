package com.luuu.seven.repository

import com.luuu.seven.bean.*
import com.luuu.seven.db.AppDatabase
import com.luuu.seven.http.HttpManager
import kotlinx.coroutines.flow.Flow


class ReadRepository {
    suspend fun getComicReadPage(comicId: Int, chapterId: Int): ComicReadBean {
        return HttpManager.getInstance.getService().getComicReadPage(comicId, chapterId)
    }

    fun isReadInChapter(comicId: Int): Flow<Boolean> {
        return AppDatabase.getInstance().historyDao().isReadInChapter(comicId)
    }

    suspend fun updateReadHistory(readHistoryBean: ReadHistoryBean) {
        return AppDatabase.getInstance().historyDao().updateReadHistory(readHistoryBean)
    }

    suspend fun insertHistory(readHistoryBean: ReadHistoryBean) {
        return AppDatabase.getInstance().historyDao().insertHistory(readHistoryBean)
    }
}
