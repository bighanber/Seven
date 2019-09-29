package com.luuu.seven.module.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luuu.seven.bean.ComicNewsFlashBean
import com.luuu.seven.bean.ComicNewsListBean
import com.luuu.seven.bean.ComicNewsPicBean
import com.luuu.seven.repository.NewsRepository
import com.luuu.seven.util.handleLoading
import com.luuu.seven.util.ioMain
import com.luuu.seven.util.launch
import com.luuu.seven.util.toast
import io.reactivex.disposables.Disposable

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

    private val _dataRefresh = MutableLiveData<Boolean>()
    val dataRefresh: LiveData<Boolean> = _dataRefresh

    private val _dataLoadMore = MutableLiveData<Boolean>()
    val dataLoadMore: LiveData<Boolean> = _dataLoadMore

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

    fun getComicNewsList(page: Int) {
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
        }
    }

    fun getComicNewsFlash(page: Int) {
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
        }
    }
}