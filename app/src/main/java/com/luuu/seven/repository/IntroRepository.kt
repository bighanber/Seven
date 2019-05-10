package com.luuu.seven.repository

import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.db.CollectDao
import com.luuu.seven.db.ReadHistoryDao
import com.luuu.seven.http.TaskData
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class IntroRepository {

    fun getComicIntro(comicId: Int): Observable<ComicIntroBean> {
       return Observable.zip(
                TaskData.getIntro(comicId),
                ReadHistoryDao.get().queryByComicId(comicId),
                BiFunction<ComicIntroBean, List<ReadHistoryBean>?, ComicIntroBean> {
                    comicIntroBean, readHistoryBeen ->
                    if (!readHistoryBeen.isEmpty()) {
                        comicIntroBean.mReadHistoryBean = readHistoryBeen[0]
                    }
                    comicIntroBean
                })
    }

    fun favoriteComic(comicId: Int, comicTitle: String, comicAuthors: String, comicCover: String, time: Long): Observable<Boolean> {
        return CollectDao.get().insert(comicId, comicTitle, comicAuthors, comicCover, time)
    }

    fun isFavorite(comicId: Int): Observable<Boolean> {
        return CollectDao.get().queryById(comicId)
    }

    fun unFavoriteComic(comicId: Int): Observable<Boolean> {
        return CollectDao.get().cancelCollect(comicId)
    }
}