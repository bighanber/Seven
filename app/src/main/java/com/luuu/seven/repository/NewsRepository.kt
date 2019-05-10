package com.luuu.seven.repository

import com.luuu.seven.bean.ComicNewsFlashBean
import com.luuu.seven.bean.ComicNewsListBean
import com.luuu.seven.bean.ComicNewsPicBean
import com.luuu.seven.http.TaskData
import io.reactivex.Observable

class NewsRepository {
    fun getComicNewsPic(): Observable<ComicNewsPicBean> {
        return TaskData.getComicNewsPic()
    }

    fun getComicNewsList(page: Int): Observable<List<ComicNewsListBean>> {
        return TaskData.getComicNewsList(page)
    }

    fun getComicNewsFlash(page: Int): Observable<List<ComicNewsFlashBean>> {
        return TaskData.getComicNewsFlash(page)
    }
}