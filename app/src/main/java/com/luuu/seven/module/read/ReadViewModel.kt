package com.luuu.seven.module.read

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luuu.seven.bean.ComicReadBean
import com.luuu.seven.bean.HotSearchBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.bean.SearchDataBean
import com.luuu.seven.repository.ReadRepository
import com.luuu.seven.repository.SearchRepository
import com.luuu.seven.util.launch
import com.luuu.seven.util.toast

class ReadViewModel : ViewModel() {

    private val mRepository by lazy { ReadRepository() }
    private var readHistoryData: ReadHistoryBean? = null

    private val _comicPageData = MutableLiveData<ComicReadBean>()
    val comicPageData: LiveData<ComicReadBean> = _comicPageData

    private val _haveRead = MutableLiveData<Boolean>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _updateOrInsert = MediatorLiveData<Boolean>()
    val updateOrInsert: LiveData<Boolean> = _updateOrInsert

    init {
        _updateOrInsert.addSource(_haveRead) {
            readHistoryData?.let { data ->
                if (it) {
                    updateReadHistory(data)
                } else {
                    insertHistory(data)
                }
            }
            _updateOrInsert.value = true
        }
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
        readHistoryData = data
        launch<Boolean> {
            request {
                mRepository.isReadInChapter(data.comicId)
            }
            onSuccess {
                _haveRead.value = it
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }

    private fun updateReadHistory(readHistoryBean: ReadHistoryBean) {
        launch<Boolean> {
            request {
                mRepository.updateReadHistory(readHistoryBean)
                return@request true
            }
            onSuccess {
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }

    private fun insertHistory(readHistoryBean: ReadHistoryBean) {
        launch<Boolean> {
            request {
                mRepository.insertHistory(readHistoryBean)
                return@request true
            }
            onSuccess {
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }

}