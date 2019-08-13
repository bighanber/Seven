package com.luuu.seven.repository

import com.luuu.seven.bean.CollectBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.db.CollectDao
import com.luuu.seven.db.ReadHistoryDao
import io.reactivex.Observable


class  ShelfRepository {

    suspend fun getReadHistory(): List<ReadHistoryBean> {
        return ReadHistoryDao.get().getReadHistory()
    }

    suspend fun getCollect(): List<CollectBean> {
        return CollectDao.get().getCollect()
    }
}