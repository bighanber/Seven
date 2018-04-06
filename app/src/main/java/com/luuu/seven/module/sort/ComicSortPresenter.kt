package com.luuu.seven.module.sort

import com.luuu.seven.http.TaskData
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by lls on 2017/8/4.
 *
 */
class ComicSortPresenter(var mView: ComicSortContract.View) : ComicSortContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getComicSort() {
        mSubscriptions.add(TaskData.getSortComic().subscribe({
            t ->
            mView.initViewPager(t)
        }, {
            mView.showLoading(false)
            mView.showError(true)
        }, {
            mView.showLoading(false)
            mView.showError(false)
        }))
    }
}