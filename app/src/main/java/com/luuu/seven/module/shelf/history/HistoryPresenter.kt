package com.luuu.seven.module.shelf.history

import com.luuu.seven.db.ReadHistoryDao
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by lls on 2017/8/9.
 *
 */
class HistoryPresenter(var mView: HistoryContract.View) : HistoryContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getComicData() {
        mSubscriptions.add(ReadHistoryDao.get().getReadHistory().subscribe({
            t ->
            mView.updateComic(t)
        }, {
            mView.showLoading(false)
            mView.showEmpty(true)
        }, {
            mView.showEmpty(false)
            mView.showLoading(false)
        }))
    }
}