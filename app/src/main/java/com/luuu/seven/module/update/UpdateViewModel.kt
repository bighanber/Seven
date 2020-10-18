package com.luuu.seven.module.update

import androidx.lifecycle.*
import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.bean.HotComicBean
import com.luuu.seven.repository.HomeRepository
import com.luuu.seven.util.Event
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UpdateViewModel : ViewModel() {

    private val mRepository by lazy { HomeRepository() }

    private val _updateData = MutableLiveData<List<ComicUpdateBean>>()

    private val _updateList = MediatorLiveData<List<ComicUpdateBean>>()
    val updateList: LiveData<List<ComicUpdateBean>>
        get() = _updateList

    private val _loadMore = MediatorLiveData<List<ComicUpdateBean>>()
    val loadMore: LiveData<List<ComicUpdateBean>>
        get() = _loadMore

    val swipeRefreshing: LiveData<Event<Boolean>> = _updateData.map { Event(false) }

    val loadMoreEnd: LiveData<Event<Boolean>> = _updateData.map {
        Event(page > 0 && it.isEmpty())
    }

    private var page = 0
    private var num = 100

    init {
        _updateList.addSource(_updateData) {
            if (page == 0 && !it.isNullOrEmpty()) {
                _updateList.value = it
            }
        }

        _loadMore.addSource(_updateData) {
            if (page > 0 && it.isNotEmpty()) {
                _loadMore.value = it
            }
        }
    }

    fun getComicUpdate() {
        viewModelScope.launch {
            mRepository.getComicUpdate(num, page).collectLatest {
                _updateData.postValue(it)
            }
        }
    }

    fun updateRefresh() {
        page = 0
        getComicUpdate()
    }

    fun updateLoadNextPage() {
        ++page
        getComicUpdate()
    }
}