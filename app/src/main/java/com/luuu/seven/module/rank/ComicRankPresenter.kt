package com.luuu.seven.module.rank

import com.luuu.seven.http.TaskData
import com.luuu.seven.util.DataLoadType
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by lls on 2017/8/4.
 *
 */
class ComicRankPresenter(var mView: ComicRankContract.View) : ComicRankContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()
    private var mIsRefresh = true

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getComicData(type: Int, page: Int) {
        mSubscriptions.add(TaskData.getRankComic(type, page).subscribe({
            t ->
            mView.updateComicList(t, if (mIsRefresh) DataLoadType.TYPE_REFRESH_SUCCESS else DataLoadType.TYPE_LOAD_MORE_SUCCESS)
        },{
            mView.judgeRefresh(false)
        },{
            mView.judgeRefresh(false)
        }))
    }

    override fun refreshData(type: Int) {
        mIsRefresh = true
        getComicData(type, 0)
    }

    override fun loadMoreData(type: Int, page: Int) {
        mIsRefresh = false
        getComicData(type, page)
    }
}