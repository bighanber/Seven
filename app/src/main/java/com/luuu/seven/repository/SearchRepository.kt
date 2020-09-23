package com.luuu.seven.repository

import com.luuu.seven.bean.*
import com.luuu.seven.http.HttpManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class SearchRepository {
    suspend fun getSearchData(keyword: String): Flow<List<SearchDataBean>> {
        return flow {
            emit(HttpManager.getInstance.getService().getSearchData(keyword))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getHotSearch(): Flow<List<HotSearchBean>> {
        return flow {
            emit(HttpManager.getInstance.getService().getHotSearch())
        }.flowOn(Dispatchers.IO)
    }

}
