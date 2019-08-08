package com.luuu.seven.module.search

import com.luuu.seven.http.TaskData
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by lls on 2017/8/9.
 *
 */
class ComicSearchPresenter(var mView: ComicSearchContract.View) : ComicSearchContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getSearchData(keyName: String) {
        mSubscriptions.add(TaskData.getSearchData(keyName).subscribe({
            t ->
            if (t.isEmpty()) {
                mView.showEmpty(true)
            } else {
                mView.showEmpty(false)
                mView.updateSearchData(t)
            }
        }, {
            mView.showLoading(false)
            mView.showError(true)
        }, {
            mView.showLoading(false)
            mView.showError(false)
        }))
    }

    override fun getHotSearch() {
        mSubscriptions.add(TaskData.getHotSearch().subscribe({
            t ->
            if (t.isEmpty()) {
                mView.showEmpty(true)
            } else {
                mView.showEmpty(false)
                mView.updateHotSearch(t)
            }
        }, {
            mView.showLoading(false)
            mView.showError(true)
        }, {
            mView.showLoading(false)
            mView.showError(false)
        }))
    }
}