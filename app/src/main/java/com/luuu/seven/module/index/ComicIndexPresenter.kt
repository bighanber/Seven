package com.luuu.seven.module.index

import com.luuu.seven.http.TaskData
import io.reactivex.disposables.CompositeDisposable


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/02
 *     desc   :
 *     version:
 */
class ComicIndexPresenter(var mView: ComicIndexContract.View) : ComicIndexContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getIndexData() {
        mSubscriptions.add(TaskData.getIndexData().subscribe({
            result ->
            mView.updateIndexList(result)
        }, {
            mView.showLoading(false)
            mView.showError(true)
            mView.judgeRefresh(false)
        }, {
            mView.showLoading(false)
            mView.showError(false)
            mView.judgeRefresh(false)
        }))
    }

    override fun refreshData() {
        getIndexData()
    }
}