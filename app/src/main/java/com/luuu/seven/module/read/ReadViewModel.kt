package com.luuu.seven.module.read

import androidx.lifecycle.*
import com.luuu.seven.bean.ComicReadBean
import com.luuu.seven.bean.HotSearchBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.bean.SearchDataBean
import com.luuu.seven.repository.ReadRepository
import com.luuu.seven.repository.SearchRepository
import com.luuu.seven.util.launch
import com.luuu.seven.util.toast
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
        launch<ComicReadBean> {
            request {
                mRepository.getComicReadPage(comicId, chapterId)
            }
            onSuccess { result ->
                _comicPageData.value = result
            }
            onFailed { error, code ->
                toast(error)
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