package com.luuu.seven.module.search

import androidx.lifecycle.*
import com.luuu.seven.bean.HotSearchBean
import com.luuu.seven.bean.SearchDataBean
import com.luuu.seven.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val mRepository by lazy { SearchRepository() }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val searchChannel = ConflatedBroadcastChannel<String>()

    private val _searchData = MutableLiveData<List<SearchDataBean>>()
    val searchData: LiveData<List<SearchDataBean>> = _searchData

    private val _hotSearchData = MutableLiveData<List<HotSearchBean>>()
    val hotSearchData: LiveData<List<HotSearchBean>> = _hotSearchData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _isEmpty = MediatorLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    init {
        _isEmpty.addSource(_searchData) {
            _isEmpty.value = it.isNullOrEmpty()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun setSearchQuery(key: String) {
        searchChannel.offer(key)
    }

    @ExperimentalCoroutinesApi
    @OptIn(FlowPreview::class)
    val searchResult = searchChannel.asFlow().flatMapLatest {
        mRepository.getSearchData(it)
    }.asLiveData()

    fun getSearchData(keyword: String) {
        viewModelScope.launch {
            mRepository.getSearchData(keyword).collectLatest {
                _searchData.value = it
            }
        }
    }

    fun getHotSearch() {
        viewModelScope.launch {
            mRepository.getHotSearch().collectLatest {
                _hotSearchData.value = it
            }
        }
    }


}