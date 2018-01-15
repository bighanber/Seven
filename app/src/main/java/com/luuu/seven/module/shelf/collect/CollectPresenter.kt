package com.luuu.seven.module.shelf.collect

import com.luuu.seven.db.CollectDao
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by lls on 2017/8/9.
 *
 */
class CollectPresenter(var mView: CollectContract.View) : CollectContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getComicCollect() {
        mSubscriptions.add(CollectDao.get().getCollect().subscribe({
            t ->
            mView.updateComicCollect(t)
        }, {
            mView.showLoading(false)
            mView.showEmpty(true)
        }, {
            mView.showEmpty(false)
            mView.showLoading(false)
        }))
    }
}