package com.luuu.seven.module.update

import com.luuu.seven.http.TaskData
import com.luuu.seven.util.DataLoadType
import io.reactivex.disposables.CompositeDisposable


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/03
 *     desc   :
 *     version:
 */
class ComicUpdatePresenter(var mView: ComicUpdateContract.View) : ComicUpdateContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()
    private var mIsRefresh = true


    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getComicUpdate(num: Int, page: Int) {
        mSubscriptions.add(TaskData.getAllUpdateComic(num, page).subscribe({
            t ->
            mView.updateComicList(t, if (mIsRefresh) DataLoadType.TYPE_REFRESH_SUCCESS else DataLoadType.TYPE_LOAD_MORE_SUCCESS)
        },{
            //onerror
            mView.judgeRefresh(false)
        },{
            //oncomplete
            mView.judgeRefresh(false)
        }))
    }

    override fun refreshData(num: Int) {
        mIsRefresh = true
        getComicUpdate(num, 0)
    }

    override fun loadMoreData(num: Int, page: Int) {
        mIsRefresh = false
        getComicUpdate(num, page)
    }
}