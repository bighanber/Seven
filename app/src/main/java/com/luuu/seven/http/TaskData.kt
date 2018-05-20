package com.luuu.seven.http

import com.luuu.seven.bean.*
import com.luuu.seven.util.SchedulerHelper
import io.reactivex.Observable

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/01
 *     desc   :
 *     version:
 */
object TaskData {
    fun getIndexData(): Observable<List<IndexBean>> {
        return HttpManager.getInstance.getService().getIndexData().compose(SchedulerHelper.io_main())
    }

    fun getAllUpdateComic(num: Int, page: Int): Observable<List<ComicUpdateBean>> {
        return HttpManager.getInstance.getService().getAllUpdataComic(num, page)
                .compose(SchedulerHelper.io_main())
    }

    fun getRankComic(type: Int, page: Int): Observable<List<HotComicBean>> {
        return HttpManager.getInstance.getService().getRankComic(type, page)
                .compose(SchedulerHelper.io_main())
    }

    fun getSortComic(): Observable<List<ComicSortBean>> {
        return HttpManager.getInstance.getService().getSortComic()
                .compose(SchedulerHelper.io_main())
    }

    fun getSortComicList(sortid: Int, page: Int): Observable<List<ComicSortListBean>> {
        return HttpManager.getInstance.getService().getSortComicList(sortid, page)
                .compose(SchedulerHelper.io_main())
    }

    fun getSpecialComic(page: Int): Observable<List<ComicSpecialBean>> {
        return HttpManager.getInstance.getService().getSpecialComic(page)
                .compose(SchedulerHelper.io_main())
    }

    fun getSpecialComicDetail(id: Int): Observable<ComicSpecialDetBean> {
        return HttpManager.getInstance.getService().getSpecialComicDetail(id)
                .compose(SchedulerHelper.io_main())
    }

    fun getIntro(comicId: Int): Observable<ComicIntroBean> {
        return HttpManager.getInstance.getService().getComicIntro(comicId)
                .compose(SchedulerHelper.io_main())
    }

    fun getComicReadPage(comicId: Int, chapterId: Int): Observable<ComicReadBean> {
        return HttpManager.getInstance.getService().getComicReadPage(comicId, chapterId)
                .compose(SchedulerHelper.io_main())
    }

    fun getComicNewsPic(): Observable<ComicNewsPicBean> {
        return HttpManager.getInstance.getService().getComicNewsPic()
                .compose(SchedulerHelper.io_main())
    }

    fun getComicNewsList(page: Int): Observable<List<ComicNewsListBean>> {
        return HttpManager.getInstance.getService().getComicNewsList(page)
                .compose(SchedulerHelper.io_main())
    }

    fun getComicNewsFlash(page: Int): Observable<List<ComicNewsFlashBean>> {
        return HttpManager.getInstance.getService().getComicNewsFlash(page)
                .compose(SchedulerHelper.io_main())
    }

    fun getSearchData(keyname: String): Observable<List<SearchDataBean>> {
        return HttpManager.getInstance.getService().getSearchData(keyname)
                .compose(SchedulerHelper.io_main())
    }

    fun getHotSearch(): Observable<List<HotSearchBean>> {
        return HttpManager.getInstance.getService().getHotSearch()
                .compose(SchedulerHelper.io_main())
    }
}