package com.luuu.seven.module.news

import androidx.lifecycle.*
import com.luuu.seven.bean.ComicNewsFlashBean
import com.luuu.seven.bean.ComicNewsListBean
import com.luuu.seven.bean.ComicNewsPicBean
import com.luuu.seven.repository.NewsRepository
import com.luuu.seven.util.*
import com.luuu.seven.util.map
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlin.Result

class NewsViewModel : ViewModel() {

    private val mRepository by lazy { NewsRepository() }

    private val _newsPicData = MutableLiveData<ComicNewsPicBean>()
    val newsPicData: LiveData<ComicNewsPicBean> = _newsPicData

    private val _newsListData = MutableLiveData<List<ComicNewsListBean>>()
    val newsListData: LiveData<List<ComicNewsListBean>> = _newsListData

    private val _newsFlashData = MutableLiveData<List<ComicNewsFlashBean>>()
    val newsFlashData: LiveData<List<ComicNewsFlashBean>> = _newsFlashData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

//    private val _dataRefresh = MutableLiveData<Boolean>()
//    val dataRefresh: LiveData<Boolean> = _dataRefresh

//    private val _dataLoadMore = MutableLiveData<Boolean>()
//    val dataLoadMore: LiveData<Boolean> = _dataLoadMore

    private val swipeRefreshResult = MutableLiveData<Boolean>()
    val swipeRefreshing: LiveData<Boolean>

    private val loadMoreResult = MutableLiveData<Boolean>()
    val loadMore: LiveData<Boolean>

    private val _isEmpty = MediatorLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    init {
        swipeRefreshing = swipeRefreshResult.map {
            false
        }

        loadMore = loadMoreResult.map {
            false
        }

        _isEmpty.addSource(_newsListData) {
            _isEmpty.value = it.isNullOrEmpty()
        }

        _isEmpty.addSource(_newsFlashData) {
            _isEmpty.value = it.isNullOrEmpty()
        }
    }

    fun getComicNewsPic() {
        viewModelScope.launch {
            mRepository.getComicNewsPic().collectLatest {
                _newsPicData.value = it
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getComicNewsList(page: Int, refresh: Boolean = false, more: Boolean = false) {
        viewModelScope.launch {
            mRepository.getComicNewsList(page).onCompletion {
                if (refresh) swipeRefreshResult.value = true
                if (more) loadMoreResult.value = true
            }.collectLatest {
                _newsListData.value = it
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getComicNewsFlash(page: Int, refresh: Boolean = false, more: Boolean = false) {
        viewModelScope.launch {
            mRepository.getComicNewsFlash(page).onCompletion {
                if (refresh) swipeRefreshResult.value = true
                if (more) loadMoreResult.value = true
            }.collectLatest {
                _newsFlashData.value = it
            }
        }
    }
}