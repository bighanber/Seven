package com.luuu.seven.module.shelf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luuu.seven.bean.CollectBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.repository.ShelfRepository
import com.luuu.seven.util.handleLoading
import com.luuu.seven.util.ioMain
import com.luuu.seven.util.toast
import io.reactivex.disposables.Disposable

class ShelfViewModel : ViewModel() {
    private val mRepository by lazy { ShelfRepository() }

    private val _historyData = MutableLiveData<List<ReadHistoryBean>>()
    val historyData: LiveData<List<ReadHistoryBean>>
        get() = _historyData

    private val _collectData = MutableLiveData<List<CollectBean>>()
    val collectData: LiveData<List<CollectBean>>
        get() = _collectData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    fun getReadHistory(showLoading: Boolean): Disposable {
        return mRepository.getReadHistory()
                .compose(ioMain())
                .compose(handleLoading(showLoading, _dataLoading))
                .subscribe({
                    _historyData.value = it
                }, {
                    toast(it.message)
                }, {})
    }

    fun getCollect(showLoading: Boolean): Disposable {
        return mRepository.getCollect()
                .compose(ioMain())
                .compose(handleLoading(showLoading, _dataLoading))
                .subscribe({
                    _collectData.value = it
                }, {
                    toast(it.message)
                }, {})
    }
}