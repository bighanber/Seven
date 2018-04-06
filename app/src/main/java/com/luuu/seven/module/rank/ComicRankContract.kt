package com.luuu.seven.module.rank

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.HotComicBean
import com.luuu.seven.util.DataLoadType

/**
 * Created by lls on 2017/8/4.
 *
 */
interface ComicRankContract {
    interface Presenter : BasePresenter {
        fun getComicData(type: Int, page: Int)
        fun refreshData(type: Int)
        fun loadMoreData(type: Int, page: Int)
    }

    interface View : BaseView<Presenter> {
        fun updateComicList(data: List<HotComicBean>, type: DataLoadType)
        fun judgeRefresh(isRefresh: Boolean)
    }
}