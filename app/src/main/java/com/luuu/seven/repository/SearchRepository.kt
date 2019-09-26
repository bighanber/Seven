package com.luuu.seven.repository

import com.luuu.seven.bean.*
import com.luuu.seven.http.HttpManager


class SearchRepository {
    suspend fun getSearchData(keyword: String): List<SearchDataBean> {
        return HttpManager.getInstance.getService().getSearchData(keyword)
    }

    suspend fun getHotSearch(): List<HotSearchBean> {
        return HttpManager.getInstance.getService().getHotSearch()
    }

}
