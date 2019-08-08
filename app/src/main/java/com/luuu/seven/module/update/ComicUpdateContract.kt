package com.luuu.seven.module.update

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.util.DataLoadType


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/03
 *     desc   :
 *     version:
 */
interface ComicUpdateContract {

    interface Presenter : BasePresenter {
        fun getComicUpdate(num: Int, page: Int)
        fun refreshData(num: Int)
        fun loadMoreData(num: Int, page: Int)
    }

    interface View : BaseView<Presenter> {
        fun updateComicList(data: List<ComicUpdateBean>, type: DataLoadType)
        fun judgeRefresh(isRefresh: Boolean)
    }
}