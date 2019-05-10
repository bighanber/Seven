package com.luuu.seven.module.news

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.luuu.seven.bean.ComicNewsFlashBean
import com.luuu.seven.bean.ComicNewsListBean
import com.luuu.seven.bean.ComicNewsPicBean
import com.luuu.seven.repository.NewsRepository
import com.luuu.seven.util.handleLoading
import com.luuu.seven.util.ioMain
import com.luuu.seven.util.toast
import io.reactivex.disposables.Disposable

class NewsViewModel : ViewModel() {

    private val mRepository by lazy { NewsRepository() }

    private val _newsPicData = MutableLiveData<ComicNewsPicBean>()
    val newsPicData: LiveData<ComicNewsPicBean>
        get() = _newsPicData

    private val _newsListData = MutableLiveData<List<ComicNewsListBean>>()
    val newsListData: LiveData<List<ComicNewsListBean>>
        get() = _newsListData

    private val _newsFlashData = MutableLiveData<List<ComicNewsFlashBean>>()
    val newsFlashData: LiveData<List<ComicNewsFlashBean>>
        get() = _newsFlashData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _dataRefresh = MutableLiveData<Boolean>()
    val dataRefresh: LiveData<Boolean>
        get() = _dataRefresh

    private val _dataLoadMore = MutableLiveData<Boolean>()
    val dataLoadMore: LiveData<Boolean>
        get() = _dataLoadMore

    fun getComicNewsPic(showLoading: Boolean): Disposable {
        return mRepository.getComicNewsPic()
                .compose(ioMain())
                .compose(handleLoading(showLoading, _dataLoading))
                .subscribe({
                    _newsPicData.value = it
                }, {
                    toast(it.message)
                }, {})
    }

    fun getComicNewsList(page: Int, showLoading: Boolean, isRefresh: Boolean = false, isLoadMore: Boolean = false): Disposable {
        return mRepository.getComicNewsList(page)
                .compose(ioMain())
                .compose(handleLoading(showLoading, _dataLoading, isRefresh, _dataRefresh, isLoadMore, _dataLoadMore))
                .subscribe({
                    _newsListData.value = it
                }, {
                    toast(it.message)
                }, {})
    }

    fun getComicNewsFlash(page: Int, showLoading: Boolean, isRefresh: Boolean = false, isLoadMore: Boolean = false): Disposable {
        return mRepository.getComicNewsFlash(page)
                .compose(ioMain())
                .compose(handleLoading(showLoading, _dataLoading, isRefresh, _dataRefresh, isLoadMore, _dataLoadMore))
                .subscribe({
                    _newsFlashData.value = it
                }, {
                    toast(it.message)
                }, {})
    }
}