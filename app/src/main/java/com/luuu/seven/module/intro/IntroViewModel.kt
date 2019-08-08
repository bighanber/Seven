package com.luuu.seven.module.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.repository.IntroRepository
import com.luuu.seven.util.handleLoading
import com.luuu.seven.util.ioMain
import com.luuu.seven.util.toast
import io.reactivex.disposables.Disposable

class IntroViewModel : ViewModel() {

    private val mRepository by lazy { IntroRepository() }

    private val _comicIntroData = MutableLiveData<ComicIntroBean>()
    val comicIntroData: LiveData<ComicIntroBean>
        get() = _comicIntroData

    private val _updateFavorite = MutableLiveData<Boolean>()
    val updateFavorite: LiveData<Boolean>
        get() = _updateFavorite

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    fun getComicIntro(comicId: Int, showLoading: Boolean): Disposable {
        return mRepository.getComicIntro(comicId)
                .compose(ioMain())
                .compose(handleLoading(showLoading, _dataLoading))
                .subscribe({
                    _comicIntroData.value = it
                }, {
                    toast(it.message)
                }, {})
    }

    fun favoriteComic(comicId: Int, comicTitle: String, comicAuthors: String, comicCover: String, time: Long): Disposable {
        return mRepository.favoriteComic(comicId, comicTitle, comicAuthors, comicCover, time)
                .compose(ioMain())
                .subscribe({
                    _updateFavorite.value = it
                }, {
                    toast(it.message)
                }, {})
    }

    fun isFavorite(comicId: Int): Disposable {
        return mRepository.isFavorite(comicId)
                .compose(ioMain())
                .subscribe({
                    _updateFavorite.value = it
                }, {
                    toast(it.message)
                }, {})
    }

    fun unFavoriteComic(comicId: Int): Disposable {
        return mRepository.unFavoriteComic(comicId)
                .compose(ioMain())
                .subscribe({
                    _updateFavorite.value = it
                }, {
                    toast(it.message)
                }, {})
    }
}