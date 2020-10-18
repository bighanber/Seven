package com.luuu.seven.module.rank

import androidx.lifecycle.*
import com.luuu.seven.bean.HotComicBean
import com.luuu.seven.repository.HomeRepository
import com.luuu.seven.util.Event
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RankViewModel : ViewModel() {

    private val mRepository by lazy { HomeRepository() }

    private val _rankData = MutableLiveData<List<HotComicBean>>()

    private val _rankList = MediatorLiveData<List<HotComicBean>>()
    val rankList: LiveData<List<HotComicBean>>
        get() = _rankList

    private val _loadMore = MediatorLiveData<List<HotComicBean>>()
    val loadMore: LiveData<List<HotComicBean>>
        get() = _loadMore

    val swipeRefreshing: LiveData<Event<Boolean>> = _rankData.map { Event(false) }

    val loadMoreEnd: LiveData<Event<Boolean>> = _rankData.map {
        Event(rankPage > 0 && it.isEmpty())
    }

    private var rankPage = 0

    init {
        _rankList.addSource(_rankData) {
            if (rankPage == 0 && !it.isNullOrEmpty()) {
                _rankList.value = it
            }
        }

        _loadMore.addSource(_rankData) {
            if (rankPage > 0 && it.isNotEmpty()) {
                _loadMore.value = it
            }
        }
    }

    fun getRankComic(type: Int) {
        viewModelScope.launch {
            mRepository.getRankComic(type, rankPage).collectLatest {
                _rankData.postValue(it)
            }
        }
    }

    fun rankRefresh(type: Int) {
        rankPage = 0
        getRankComic(type)
    }

    fun rankLoadNextPage(type: Int) {
        ++rankPage
        getRankComic(type)
    }
}