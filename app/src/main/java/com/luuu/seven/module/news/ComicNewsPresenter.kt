package com.luuu.seven.module.news

import com.luuu.seven.http.TaskData
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by lls on 2017/8/9.
 *
 */
class ComicNewsPresenter(var mView: ComicNewsContract.View) : ComicNewsContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getComicData() {
        mSubscriptions.add(TaskData.getComicNewsPic().subscribe({
            t ->
            mView.updateComicList(t)
        }, {
            mView.showLoading(false)
            mView.showError(true)
        }, {
            mView.showLoading(false)
            mView.showError(false)
        }))
    }
}