package com.luuu.seven.repository

import com.luuu.seven.bean.ComicNewsFlashBean
import com.luuu.seven.bean.ComicNewsListBean
import com.luuu.seven.bean.ComicNewsPicBean
import com.luuu.seven.http.HttpManager
import com.luuu.seven.http.TaskData
import io.reactivex.Observable


class NewsRepository {
    suspend fun getComicNewsPic(): ComicNewsPicBean {
        return HttpManager.getInstance.getService().getComicNewsPic()
    }

    suspend fun getComicNewsList(page: Int): List<ComicNewsListBean> {
        return HttpManager.getInstance.getService().getComicNewsList(page)
    }

    suspend fun getComicNewsFlash(page: Int): List<ComicNewsFlashBean> {
        return HttpManager.getInstance.getService().getComicNewsFlash(page)
    }
}