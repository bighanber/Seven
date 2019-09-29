package com.luuu.seven.repository

import com.luuu.seven.bean.CollectBean
import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.bean.ComicRelatedInfoBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.db.AppDatabase
import com.luuu.seven.db.CollectDao
import com.luuu.seven.db.ReadHistoryDao
import com.luuu.seven.http.HttpManager
import com.luuu.seven.http.TaskData
import io.reactivex.Observable
import io.reactivex.functions.BiFunction


class IntroRepository {

    suspend fun getComicIntro(comicId: Int): ComicIntroBean {
        return HttpManager.getInstance.getService().getComicIntro(comicId)
    }

    suspend fun getComicRelated(comicId: Int): ComicRelatedInfoBean {
        return HttpManager.getInstance.getService().getComicRelated(comicId)
    }

    suspend fun getReadHistory(comicId: Int): List<ReadHistoryBean> {
        return AppDatabase.getInstance().historyDao().queryByComicId(comicId)
    }

    suspend fun favoriteComic(collectBean: CollectBean) {
        return AppDatabase.getInstance().collectionDao().insertCollection(collectBean)
    }

    suspend fun isFavorite(comicId: Int): Boolean {
        return AppDatabase.getInstance().collectionDao().getCollectionById(comicId)
    }

    suspend fun unFavoriteComic(comicId: Int) {
        return AppDatabase.getInstance().collectionDao().cancelCollection(comicId)
    }
}