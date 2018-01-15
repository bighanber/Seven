package com.luuu.seven.module.sort

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.ComicSortBean

/**
 * Created by lls on 2017/8/4.
 *
 */
interface ComicSortContract {
    interface Presenter : BasePresenter {
        fun getComicSort()
    }

    interface View : BaseView<Presenter> {
        fun initViewPager(data: List<ComicSortBean>)
    }
}