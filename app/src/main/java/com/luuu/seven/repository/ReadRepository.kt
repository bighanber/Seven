package com.luuu.seven.repository

import com.luuu.seven.bean.*
import com.luuu.seven.db.AppDatabase
import com.luuu.seven.http.HttpManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class ReadRepository {
    suspend fun getComicReadPage(comicId: Int, chapterId: Int): Flow<ComicReadBean> {
        return flow {
            emit(HttpManager.getInstance.getService().getComicReadPage(comicId, chapterId))
        }.flowOn(Dispatchers.IO)
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
