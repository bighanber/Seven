package com.luuu.seven.repository

import com.luuu.seven.bean.IndexBean
import com.luuu.seven.http.HttpManager


class HomeRepository {
    suspend fun getHomeData(): List<IndexBean> {
        return HttpManager.getInstance.getService().getIndexData()
    }
}