package com.luuu.seven.module.read

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luuu.seven.bean.ComicReadBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.repository.ReadRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReadViewModel : ViewModel() {

    private val mRepository by lazy { ReadRepository() }
    private var readHistoryData: ReadHistoryBean? = null

    private val _comicPageData = MutableLiveData<ComicReadBean>()
    val comicPageData: LiveData<ComicReadBean> = _comicPageData

    private val _updateOrInsert = MutableLiveData<Boolean>()
    val updateOrInsert: LiveData<Boolean> = _updateOrInsert

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    init {

    }

    fun getComicReadPage(comicId: Int, chapterId: Int) {
        viewModelScope.launch {
            mRepository.getComicReadPage(comicId, chapterId).collectLatest {
                _comicPageData.value = it
            }
        }
    }

    fun updateOrInsertReadData(data: ReadHistoryBean) {
        viewModelScope.launch {
            mRepository.isReadInChapter(data.comicId).collectLatest {
                if (it) {
                    updateReadHistory(data)
                } else {
                    insertHistory(data)
                }
                _updateOrInsert.value = true
            }
        }
    }

    private fun updateReadHistory(readHistoryBean: ReadHistoryBean) {
        viewModelScope.launch {
            mRepository.updateReadHistory(readHistoryBean)
        }
    }

    private fun insertHistory(readHistoryBean: ReadHistoryBean) {
        viewModelScope.launch {
            mRepository.insertHistory(readHistoryBean)
        }
    }



}