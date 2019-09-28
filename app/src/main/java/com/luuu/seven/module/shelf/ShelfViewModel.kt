package com.luuu.seven.module.shelf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luuu.seven.bean.CollectBean
import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.repository.ShelfRepository
import com.luuu.seven.util.handleLoading
import com.luuu.seven.util.ioMain
import com.luuu.seven.util.launch
import com.luuu.seven.util.toast
import io.reactivex.disposables.Disposable

class ShelfViewModel : ViewModel() {
    private val mRepository by lazy { ShelfRepository() }

    private val _historyData = MutableLiveData<List<ReadHistoryBean>>()
    val historyData: LiveData<List<ReadHistoryBean>> = _historyData

    private val _collectData = MutableLiveData<List<CollectBean>>()
    val collectData: LiveData<List<CollectBean>> = _collectData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun getReadHistory() {
        launch<List<ReadHistoryBean>> {
            request {
                mRepository.getReadHistory()
            }
            onSuccess { result ->
                _historyData.value = result
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }

    fun getCollect() {
        launch<List<CollectBean>> {
            request {
                mRepository.getCollect()
            }
            onSuccess { result ->
                _collectData.value = result
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }
}