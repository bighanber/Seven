package com.luuu.seven.module.news

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.ComicNewsPicBean



/**
 * Created by lls on 2017/8/9.
 *
 */
interface ComicNewsContract {
    interface Presenter : BasePresenter {
        fun getComicData()
    }

    interface View : BaseView<Presenter> {
        fun updateComicList(data: ComicNewsPicBean)
    }
}