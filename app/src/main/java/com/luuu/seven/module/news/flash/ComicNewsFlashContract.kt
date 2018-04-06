package com.luuu.seven.module.news.flash

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.ComicNewsFlashBean
import com.luuu.seven.util.DataLoadType

/**
 * Created by lls on 2017/8/9.
 *
 */
interface ComicNewsFlashContract {
    interface Presenter : BasePresenter {
        fun getComicData(page: Int)
        fun refreshData()
        fun loadMoreData(page: Int)
    }

    interface View : BaseView<Presenter> {
        fun updateComicList(data: List<ComicNewsFlashBean>, type: DataLoadType)
        fun judgeRefresh(isRefresh: Boolean)
    }
}