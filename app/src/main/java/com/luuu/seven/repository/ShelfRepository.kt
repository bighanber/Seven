package com.luuu.seven.repository

import com.luuu.seven.bean.CollectBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.db.AppDatabase
import com.luuu.seven.db.CollectDao
import com.luuu.seven.db.ReadHistoryDao
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow


class  ShelfRepository {

    fun getReadHistory(): Flow<List<ReadHistoryBean>> {
        return AppDatabase.getInstance().historyDao().getReadHistory()
    }

    fun getCollect(): Flow<List<CollectBean>> {
        return AppDatabase.getInstance().collectionDao().getCollectionData()
    }
}