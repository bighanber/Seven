package com.luuu.seven.repository

import com.luuu.seven.bean.*
import com.luuu.seven.http.HttpManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class SortRepository {

    suspend fun getSortComicFilter(): Flow<List<SortFilterBean>> {
        return flow {
            emit(HttpManager.getInstance.getService().getSortComicFilter())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSortComicList(filter: String, page: Int): Flow<List<ComicSortListBean>> {
        return flow {
            emit(HttpManager.getInstance.getService().getSortComicList(filter, page))
        }.flowOn(Dispatchers.IO)
    }

}
