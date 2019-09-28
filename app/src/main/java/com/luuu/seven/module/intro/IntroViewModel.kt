package com.luuu.seven.module.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.bean.ComicRelatedInfoBean
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.repository.IntroRepository
import com.luuu.seven.util.handleLoading
import com.luuu.seven.util.ioMain
import com.luuu.seven.util.launch
import com.luuu.seven.util.toast
import io.reactivex.disposables.Disposable

class IntroViewModel : ViewModel() {

    private val mRepository by lazy { IntroRepository() }

    private val _comicIntroData = MutableLiveData<ComicIntroBean>()
    val comicIntroData: LiveData<ComicIntroBean> = _comicIntroData

    private val _updateFavorite = MutableLiveData<Boolean>()
    val updateFavorite: LiveData<Boolean> = _updateFavorite

    private val _readHistory = MutableLiveData<List<ReadHistoryBean>>()
    val readHistory: LiveData<List<ReadHistoryBean>> = _readHistory

    private val _comicRelatedData = MutableLiveData<ComicRelatedInfoBean>()
    val comicRelatedData: LiveData<ComicRelatedInfoBean> = _comicRelatedData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun getComicIntro(comicId: Int) {
        launch<ComicIntroBean> {
            request {
                mRepository.getComicIntro(comicId)
            }
            onSuccess { result ->
                _comicIntroData.value = result
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }

    fun getComicRelated(comicId: Int) {
        launch<ComicRelatedInfoBean> {
            request {
                mRepository.getComicRelated(comicId)
            }
            onSuccess { result ->
                _comicRelatedData.value = result
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }

    fun getReadHistory(comicId: Int) {
        launch<List<ReadHistoryBean>> {
            request {
                mRepository.getReadHistory(comicId)
            }
            onSuccess { result ->
                _readHistory.value = result
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }

    fun favoriteComic(comicId: Int, comicTitle: String, comicAuthors: String, comicCover: String, time: Long) {
        launch<Boolean> {
            request {
                mRepository.favoriteComic(comicId, comicTitle, comicAuthors, comicCover, time)
            }
            onSuccess { result ->
                _updateFavorite.value = result
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }

    fun isFavorite(comicId: Int) {
        launch<Boolean> {
            request {
                mRepository.isFavorite(comicId)
            }
            onSuccess { result ->
                _updateFavorite.value = result
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }

    fun unFavoriteComic(comicId: Int) {
        launch<Boolean> {
            request {
                mRepository.unFavoriteComic(comicId)
            }
            onSuccess { result ->
                _updateFavorite.value = result
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }
}