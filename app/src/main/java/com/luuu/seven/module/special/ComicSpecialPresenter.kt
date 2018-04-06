package com.luuu.seven.module.special

import com.luuu.seven.http.TaskData
import com.luuu.seven.util.DataLoadType
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by lls on 2017/8/4.
 *
 */
class ComicSpecialPresenter(var mView: ComicSpecialContract.View) : ComicSpecialContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()
    private var mIsRefresh = true

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getComicSpecial(page: Int) {
        mSubscriptions.add(TaskData.getSpecialComic(page).subscribe({
            t ->
            mView.updateComicList(t, if (mIsRefresh) DataLoadType.TYPE_REFRESH_SUCCESS else DataLoadType.TYPE_LOAD_MORE_SUCCESS)
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
        mIsRefresh = true
        getComicSpecial(0)
    }

    override fun loadMoreData(page: Int) {
        mIsRefresh = false
        getComicSpecial(page)
    }

}