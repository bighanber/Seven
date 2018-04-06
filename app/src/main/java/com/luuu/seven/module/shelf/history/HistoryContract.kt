package com.luuu.seven.module.shelf.history

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.ReadHistoryBean

/**
 * Created by lls on 2017/8/9.
 *
 */
interface HistoryContract {
    interface Presenter : BasePresenter {
        fun getComicData()
    }

    interface View : BaseView<Presenter> {
        fun updateComic(data: List<ReadHistoryBean>)
    }
}