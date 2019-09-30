package com.luuu.seven.module.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luuu.seven.bean.ComicNewsFlashBean
import com.luuu.seven.bean.ComicNewsListBean
import com.luuu.seven.bean.ComicNewsPicBean
import com.luuu.seven.repository.NewsRepository
import com.luuu.seven.util.*
import io.reactivex.disposables.Disposable
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
        launch<ComicNewsPicBean> {
            request {
                mRepository.getComicNewsPic()
            }
            onSuccess {
                _newsPicData.value = it
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }

    fun getComicNewsList(page: Int, refresh: Boolean = false, more: Boolean = false) {
        launch<List<ComicNewsListBean>> {
            request {
                mRepository.getComicNewsList(page)
            }
            onSuccess {
                _newsListData.value = it
            }
            onFailed { error, code ->
                toast(error)
            }
            onComplete {
                if (refresh) swipeRefreshResult.value = true
                if (more) loadMoreResult.value = true
            }
        }
    }

    fun getComicNewsFlash(page: Int, refresh: Boolean = false, more: Boolean = false) {
        launch<List<ComicNewsFlashBean>> {
            request {
                mRepository.getComicNewsFlash(page)
            }
            onSuccess {
                _newsFlashData.value = it
            }
            onFailed { error, code ->
                toast(error)
            }
            onComplete {
                if (refresh) swipeRefreshResult.value = true
                if (more) loadMoreResult.value = true
            }
        }
    }
}