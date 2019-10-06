package com.luuu.seven.repository

import com.luuu.seven.bean.*
import com.luuu.seven.http.HttpManager


class SortRepository {

    suspend fun getSortComicFilter(): List<SortFilterBean> {
        return HttpManager.getInstance.getService().getSortComicFilter()
    }

    suspend fun getSortComicList(filter: String, page: Int): List<ComicSortListBean> {
        return HttpManager.getInstance.getService().getSortComicList(filter, page)
    }

}
