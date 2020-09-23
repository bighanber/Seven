package com.luuu.seven.module.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luuu.seven.bean.CollectBean
import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.bean.ComicRelatedInfoBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.repository.IntroRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            mRepository.getComicIntro(comicId).collectLatest {
                _comicIntroData.value = it
            }
        }
    }

    fun getComicRelated(comicId: Int) {
        viewModelScope.launch {
            mRepository.getComicRelated(comicId).collectLatest {
                _comicRelatedData.value = it
            }
        }
    }

    fun getReadHistory(comicId: Int) {
        viewModelScope.launch {
            mRepository.getReadHistory(comicId).collectLatest {
                _readHistory.value = it
            }
        }
    }

    fun favoriteComic(comicId: Int, comicTitle: String, comicAuthors: String, comicCover: String, time: Long) {
        viewModelScope.launch {
            mRepository.favoriteComic(CollectBean(comicId, comicTitle, comicAuthors, comicCover, time))
            _updateFavorite.value = true
        }
    }

    fun isFavorite(comicId: Int) {
        viewModelScope.launch {
            mRepository.isFavorite(comicId).collectLatest {
                _updateFavorite.value = it
            }
        }
    }

    fun unFavoriteComic(comicId: Int) {
        viewModelScope.launch {
            mRepository.unFavoriteComic(comicId)
            _updateFavorite.value = false
        }
    }


}