package com.luuu.seven.module.sort.sortlist

import com.luuu.seven.http.TaskData
import com.luuu.seven.util.DataLoadType
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by lls on 2017/8/4.
 *
 */
class ComicSortListPresenter(var mView: ComicSortListContract.View) : ComicSortListContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()
    private var mIsRefresh = true

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getComicSortList(sortId: Int, page: Int) {
        mSubscriptions.add(TaskData.getSortComicList(sortId, page).subscribe({
            t ->
            mView.updateComicSortList(t, if (mIsRefresh) DataLoadType.TYPE_REFRESH_SUCCESS else DataLoadType.TYPE_LOAD_MORE_SUCCESS)
        }, {
            mView.showLoading(false)
            mView.judgeRefresh(false)
            mView.showError(true)
        }, {
            mView.showLoading(false)
            mView.showError(false)
            mView.judgeRefresh(false)
        }))
    }

    override fun refreshData(sortId: Int) {
        mIsRefresh = true
        getComicSortList(sortId, 0)
    }

    override fun loadMoreData(sortId: Int, page: Int) {
        mIsRefresh = false
        getComicSortList(sortId, page)
    }
}