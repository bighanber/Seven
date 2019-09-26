package com.luuu.seven.module.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luuu.seven.bean.HotSearchBean
import com.luuu.seven.bean.SearchDataBean
import com.luuu.seven.repository.SearchRepository
import com.luuu.seven.util.launch
import com.luuu.seven.util.toast

class SearchViewModel : ViewModel() {

    private val mRepository by lazy { SearchRepository() }

    private val _searchData = MutableLiveData<List<SearchDataBean>>()
    val searchData: LiveData<List<SearchDataBean>>
        get() = _searchData

    private val _hotSearchData = MutableLiveData<List<HotSearchBean>>()
    val hotSearchData: LiveData<List<HotSearchBean>>
        get() = _hotSearchData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    fun getSearchData(keyword: String) {
        launch<List<SearchDataBean>> {
            request {
                mRepository.getSearchData(keyword)
            }
            onSuccess { result ->
                _searchData.value = result
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }

    fun getHotSearch() {
        launch<List<HotSearchBean>> {
            request {
                mRepository.getHotSearch()
            }
            onSuccess {
                _hotSearchData.value = it
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }


}