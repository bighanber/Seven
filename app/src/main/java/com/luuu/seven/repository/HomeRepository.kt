package com.luuu.seven.repository

import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.bean.HotComicBean
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.http.HttpManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class HomeRepository {
    suspend fun getHomeData(): Flow<List<IndexBean>> {
        return flow {
            val data = HttpManager.getInstance.getService().getIndexData()
            emit(data)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getComicUpdate(num: Int, page: Int): Flow<List<ComicUpdateBean>> {
        return flow {
            emit(HttpManager.getInstance.getService().getAllUpdataComic(num, page))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getRankComic(type: Int, page: Int): Flow<List<HotComicBean>> {
        return flow {
            emit(HttpManager.getInstance.getService().getRankComic(type, page))
        }.flowOn(Dispatchers.IO)
    }
}
