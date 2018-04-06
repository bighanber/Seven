package com.luuu.seven.module.intro

import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.db.CollectDao
import com.luuu.seven.db.ReadHistoryDao
import com.luuu.seven.http.TaskData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

/**
 * Created by lls on 2017/8/7.
 *
 */
class ComicIntroPresenter(var mView: ComicIntroContract.View) : ComicIntroContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getComicIntro(comicId: Int) {
        mSubscriptions.add(Observable.zip(
                TaskData.getIntro(comicId),
                ReadHistoryDao.get().queryByComicId(comicId),
                BiFunction<ComicIntroBean, List<ReadHistoryBean>?, ComicIntroBean> {
                    comicIntroBean, readHistoryBeen ->
                    if (!readHistoryBeen.isEmpty()) {
                        comicIntroBean.mReadHistoryBean = readHistoryBeen[0]
                    }
                    comicIntroBean
                })
                .subscribe({
                    comicIntroBean ->
                        mView.updateComicData(comicIntroBean)
                }, {
                    mView.showLoading(false)
                    mView.showError(true)
                }, {
                    mView.showLoading(false)
                    mView.showError(false)
                }))
    }

    override fun favoriteComic(comicId: Int, comicTitle: String, comicAuthors: String, comicCover: String, time: Long) {
        mSubscriptions.add(CollectDao.get().insert(comicId, comicTitle, comicAuthors, comicCover, time)
                .subscribe({
                    t ->  mView.updateFavoriteMenu(t)
                }, {}, {}))
    }

    override fun isFavorite(comicId: Int) {
        mSubscriptions.add(CollectDao.get().queryById(comicId)
                .subscribe({
                    t ->  mView.updateFavoriteMenu(t)
                }, {}, {}))
    }

    override fun unFavoriteComic(comicId: Int) {
        mSubscriptions.add(CollectDao.get().cancelCollect(comicId)
                .subscribe({
                    t ->  mView.updateFavoriteMenu(!t)
                }, {}, {}))
    }
}