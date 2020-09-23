package com.luuu.seven.module.shelf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luuu.seven.bean.CollectBean
import com.luuu.seven.bean.ComicIntroBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.repository.ShelfRepository
import com.luuu.seven.util.handleLoading
import com.luuu.seven.util.ioMain
import com.luuu.seven.util.launch
import com.luuu.seven.util.toast
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ShelfViewModel : ViewModel() {
    private val mRepository by lazy { ShelfRepository() }

    private val _historyData = MutableLiveData<List<ReadHistoryBean>>()
    val historyData: LiveData<List<ReadHistoryBean>> = _historyData

    private val _collectData = MutableLiveData<List<CollectBean>>()
    val collectData: LiveData<List<CollectBean>> = _collectData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun getReadHistory() {
        viewModelScope.launch {
            mRepository.getReadHistory().collectLatest {
                _historyData.value = it
            }
        }
    }

    fun getCollect() {
        viewModelScope.launch {
            mRepository.getCollect().collectLatest {
                _collectData.value = it
            }
        }
    }
}