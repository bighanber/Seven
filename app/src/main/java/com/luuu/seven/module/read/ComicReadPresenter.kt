package com.luuu.seven.module.read

import com.luuu.seven.db.ReadHistoryDao
import com.luuu.seven.http.TaskData
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by lls on 2017/8/7.
 */
class ComicReadPresenter(var mView: ComicReadContract.View) : ComicReadContract.Presenter {

    private var mSubscriptions: CompositeDisposable = CompositeDisposable()

    override fun unsubscribe() {
        mSubscriptions.clear()
    }

    override fun getComicData(comicId: Int, chapterId: Int) {
        mSubscriptions.add(TaskData.getComicReadPage(comicId, chapterId).subscribe({
            t ->
            mView.updateComicContent(t.pageUrl, null, false)
        }, {
            mView.showError(true)
        }, {
            mView.showError(false)
        }))
    }

    override fun updateReadHistory(comicId: Int, chapterId: Int, chapterTitle: String, browsePosition: Int, cover: String, title: String) {
//        ReadHistoryDao.get().isReadInChapter(comicId).flatMap { t ->
//            if (t) {
//                ReadHistoryDao.get()
//                        .updateReadHistory(comicId, chapterId, chapterTitle, browsePosition, cover, title)
//            } else {
//                ReadHistoryDao.get()
//                        .insertHistory(comicId, chapterId, chapterTitle, browsePosition, cover, title)
//            }
//        }.subscribe({
//            t ->
//            mView.isSuccess(t)
//        }, {
//            mView.showError(true)
//        }, {
//            mView.showError(false)
//        })
    }
}