package com.luuu.seven.module.special.detail

import com.luuu.seven.http.TaskData
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by lls on 2017/8/4.
 *
 */
class ComicSpecialDetailPresenter(var mView: ComicSpecialDetailContract.View) : ComicSpecialDetailContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getComicSpecialDetail(id: Int) {
        TaskData.getSpecialComicDetail(id).subscribe({
            t ->  mView.updateComicList(t)
        }, {
            mView.showLoading(false)
            mView.showError(true)
        }, {
            mView.showLoading(false)
            mView.showError(false)
        })
    }
}