package com.luuu.seven.repository

import com.luuu.seven.bean.*
import com.luuu.seven.http.HttpManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class SpecialRepository {
    suspend fun getSpecialComic(page: Int): Flow<List<ComicSpecialBean>> {
        return flow {
            val data = HttpManager.getInstance.getService().getSpecialComic(page)
            emit(data)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSpecialComicDetail(id: Int): Flow<ComicSpecialDetBean> {
        return flow {
            emit(HttpManager.getInstance.getService().getSpecialComicDetail(id))
        }.flowOn(Dispatchers.IO)
    }
}
