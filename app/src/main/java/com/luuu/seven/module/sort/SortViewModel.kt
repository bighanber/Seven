package com.luuu.seven.module.sort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luuu.seven.bean.ComicSortListBean
import com.luuu.seven.bean.SortFilterBean
import com.luuu.seven.repository.SortRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SortViewModel : ViewModel() {
    private val mRepository by lazy { SortRepository() }

    private val _sortFilterData = MutableLiveData<List<SortFilterBean>>()
    val sortFilterData: LiveData<List<SortFilterBean>> = _sortFilterData

    private val _sortListData = MutableLiveData<List<ComicSortListBean>>()
    val sortListData: LiveData<List<ComicSortListBean>> = _sortListData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun getSortComicFilter() {
        viewModelScope.launch {
            mRepository.getSortComicFilter().collectLatest {
                _sortFilterData.value = it
            }
        }
    }

    fun getSortComicList(filter: String, page: Int) {
        viewModelScope.launch {
            mRepository.getSortComicList(filter, page).collectLatest {
                _sortListData.value = it
            }
        }
    }
}