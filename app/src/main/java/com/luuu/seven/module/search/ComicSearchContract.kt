package com.luuu.seven.module.search

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.HotSearchBean
import com.luuu.seven.bean.SearchDataBean


/**
 * Created by lls on 2017/8/9.
 *
 */
interface ComicSearchContract {
    interface Presenter : BasePresenter {
        fun getSearchData(keyName: String)
        fun getHotSearch()
    }

    interface View : BaseView<Presenter> {
        fun updateSearchData(dataBeen: List<SearchDataBean>)
        fun updateHotSearch(data: List<HotSearchBean>)
    }
}