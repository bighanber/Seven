package com.luuu.seven.repository

import com.luuu.seven.bean.IndexBean
import com.luuu.seven.http.HttpManager
import io.reactivex.Observable

class HomeRepository {
    fun getHomeData(): Observable<List<IndexBean>> {
        return HttpManager.getInstance.getService().getIndexData()
    }
}