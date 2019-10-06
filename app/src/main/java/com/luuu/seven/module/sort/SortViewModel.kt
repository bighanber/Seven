package com.luuu.seven.module.sort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luuu.seven.bean.*
import com.luuu.seven.repository.ShelfRepository
import com.luuu.seven.repository.SortRepository
import com.luuu.seven.util.handleLoading
import com.luuu.seven.util.ioMain
import com.luuu.seven.util.launch
import com.luuu.seven.util.toast
import io.reactivex.disposables.Disposable

class SortViewModel : ViewModel() {
    private val mRepository by lazy { SortRepository() }

    private val _sortFilterData = MutableLiveData<List<SortFilterBean>>()
    val sortFilterData: LiveData<List<SortFilterBean>> = _sortFilterData

    private val _sortListData = MutableLiveData<List<ComicSortListBean>>()
    val sortListData: LiveData<List<ComicSortListBean>> = _sortListData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun getSortComicFilter() {
        launch<List<SortFilterBean>> {
            request {
                mRepository.getSortComicFilter()
            }
            onSuccess { result ->
                _sortFilterData.value = result
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }

    fun getSortComicList(filter: String, page: Int) {
        launch<List<ComicSortListBean>> {
            request {
                mRepository.getSortComicList(filter, page)
            }
            onSuccess { result ->
                _sortListData.value = result
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }
}