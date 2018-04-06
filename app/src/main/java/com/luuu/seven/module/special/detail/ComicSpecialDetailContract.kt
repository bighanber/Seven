package com.luuu.seven.module.special.detail

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.ComicSpecialDetBean

/**
 * Created by lls on 2017/8/4.
 *
 */
interface ComicSpecialDetailContract {
    interface Presenter : BasePresenter {
        fun getComicSpecialDetail(id: Int)
    }

    interface View : BaseView<Presenter> {
        fun updateComicList(data: ComicSpecialDetBean)
    }
}