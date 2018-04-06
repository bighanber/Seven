package com.luuu.seven.module.sort.sortlist

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.ComicSortListBean
import com.luuu.seven.util.DataLoadType

/**
 * Created by lls on 2017/8/4.
 *
 */
interface ComicSortListContract {
    interface Presenter : BasePresenter {
        fun getComicSortList(sortId: Int, page: Int)
        fun refreshData(sortId: Int)
        fun loadMoreData(sortId: Int, page: Int)
    }

    interface View : BaseView<Presenter> {
        fun updateComicSortList(data: List<ComicSortListBean>, type: DataLoadType)
        fun judgeRefresh(isRefresh: Boolean)
    }
}