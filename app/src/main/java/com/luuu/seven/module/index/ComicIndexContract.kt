package com.luuu.seven.module.index

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.IndexBean

/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/02
 *     desc   :
 *     version:
 */
interface ComicIndexContract {
    interface Presenter : BasePresenter {
        fun getIndexData()
        fun refreshData()
    }

    interface View : BaseView<Presenter> {
        fun updateIndexList(data: List<IndexBean>)
        fun judgeRefresh(isRefresh: Boolean)
    }
}