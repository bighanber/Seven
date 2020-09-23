package com.luuu.seven.repository

import com.luuu.seven.bean.CollectBean
import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.bean.ComicRelatedInfoBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.db.AppDatabase
import com.luuu.seven.http.HttpManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class IntroRepository {

    suspend fun getComicIntro(comicId: Int): Flow<ComicIntroBean> {
        return flow {
            emit(HttpManager.getInstance.getService().getComicIntro(comicId))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getComicRelated(comicId: Int): Flow<ComicRelatedInfoBean> {
        return flow {
            emit(HttpManager.getInstance.getService().getComicRelated(comicId))
        }.flowOn(Dispatchers.IO)
    }

    fun getReadHistory(comicId: Int): Flow<List<ReadHistoryBean>> {
        return AppDatabase.getInstance().historyDao().queryByComicId(comicId)
    }

    suspend fun favoriteComic(collectBean: CollectBean) {
        return AppDatabase.getInstance().collectionDao().insertCollection(collectBean)
    }

    fun isFavorite(comicId: Int): Flow<Boolean> {
        return AppDatabase.getInstance().collectionDao().getCollectionById(comicId)
    }

    suspend fun unFavoriteComic(comicId: Int) {
        return AppDatabase.getInstance().collectionDao().cancelCollection(comicId)
    }

}