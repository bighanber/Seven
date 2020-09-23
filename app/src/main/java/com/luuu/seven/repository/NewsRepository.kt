package com.luuu.seven.repository

import com.luuu.seven.bean.ComicNewsFlashBean
import com.luuu.seven.bean.ComicNewsListBean
import com.luuu.seven.bean.ComicNewsPicBean
import com.luuu.seven.http.HttpManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class NewsRepository {
    suspend fun getComicNewsPic(): Flow<ComicNewsPicBean> {
        return flow {
            emit(HttpManager.getInstance.getService().getComicNewsPic())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getComicNewsList(page: Int): Flow<List<ComicNewsListBean>> {
        return flow {
            emit(HttpManager.getInstance.getService().getComicNewsList(page))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getComicNewsFlash(page: Int): Flow<List<ComicNewsFlashBean>> {
        return flow {
            emit(HttpManager.getInstance.getService().getComicNewsFlash(page))
        }.flowOn(Dispatchers.IO)
    }
}