package com.luuu.seven.repository

import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.bean.HotComicBean
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.http.HttpManager


class HomeRepository {
    suspend fun getHomeData(): List<IndexBean> {
        return HttpManager.getInstance.getService().getIndexData()
    }

    suspend fun getComicUpdate(num: Int, page: Int): List<ComicUpdateBean> {
        return HttpManager.getInstance.getService().getAllUpdataComic(num, page)
    }

    suspend fun getRankComic(type: Int, page: Int): List<HotComicBean> {
        return HttpManager.getInstance.getService().getRankComic(type, page)
    }
}
