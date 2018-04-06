package com.luuu.seven.module.special

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.ComicSpecialBean
import com.luuu.seven.util.DataLoadType



/**
 * Created by lls on 2017/8/4.
 *
 */
interface ComicSpecialContract {
    interface Presenter : BasePresenter {
        fun getComicSpecial(page: Int)
        fun refreshData()
        fun loadMoreData(page: Int)
    }

    interface View : BaseView<Presenter> {
        fun updateComicList(data: List<ComicSpecialBean>, type: DataLoadType)
        fun judgeRefresh(isRefresh: Boolean)
    }
}