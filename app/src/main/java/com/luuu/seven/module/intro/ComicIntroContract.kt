package com.luuu.seven.module.intro

import com.luuu.seven.base.BasePresenter
import com.luuu.seven.base.BaseView
import com.luuu.seven.bean.ComicIntroBean

/**
 * Created by lls on 2017/8/7.
 *
 */
interface ComicIntroContract {
    interface Presenter : BasePresenter {
        fun getComicIntro(comicId: Int)
        fun favoriteComic(comicId: Int, comicTitle: String, comicAuthors: String, comicCover: String, time: Long)
        fun isFavorite(comicId: Int)
        fun unFavoriteComic(comicId: Int)
    }

    interface View : BaseView<Presenter> {
        fun updateComicData(data: ComicIntroBean)
        fun updateFavoriteMenu(isFavorite: Boolean)
    }
}