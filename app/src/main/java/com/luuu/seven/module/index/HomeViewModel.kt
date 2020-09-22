package com.luuu.seven.module.index

import android.os.Bundle
import androidx.lifecycle.*
import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.bean.HotComicBean
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.repository.HomeRepository
import com.luuu.seven.util.Event
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val mRepository by lazy { HomeRepository() }

    private val _homeData = MutableLiveData<List<IndexBean>>()

    private val _homeList = MediatorLiveData<List<IndexBean>>()
    val homeList: LiveData<List<IndexBean>>
        get() = _homeList

    private val _homeBanner = MediatorLiveData<IndexBean>()
    val homeBanner: LiveData<IndexBean>
        get() = _homeBanner

    private val _updateData = MutableLiveData<List<ComicUpdateBean>>()
    val updateData: LiveData<List<ComicUpdateBean>>
        get() = _updateData

    private val _rankData = MutableLiveData<List<HotComicBean>>()
    val rankData: LiveData<List<HotComicBean>>
        get() = _rankData

    private val _toSpecialDetail = MutableLiveData<Event<Bundle>>()
    val toSpecialDetail: LiveData<Event<Bundle>> = _toSpecialDetail

    private val _toWeb = MutableLiveData<Event<Bundle>>()
    val toWeb: LiveData<Event<Bundle>> = _toWeb

    private val _toIntro = MutableLiveData<Event<Bundle>>()
    val toIntro: LiveData<Event<Bundle>> = _toIntro

    init {
        _homeList.addSource(_homeData) {
            _homeList.value = it.filterNot { it.sort == 1 || it.sort == 4 }
        }

        _homeBanner.addSource(_homeData) {
            _homeBanner.value = it.find { it.sort == 1 }
        }
    }

    fun getHomeData() {
        viewModelScope.launch {
            mRepository.getHomeData().collectLatest {
                _homeData.value = it
            }
        }
    }

    fun getComicUpdate(num: Int, page: Int) {
        viewModelScope.launch {
            mRepository.getComicUpdate(num, page).collectLatest {
                _updateData.postValue(it)
            }
        }
    }

    fun getRankComic(type: Int, page: Int) {
        viewModelScope.launch {
            mRepository.getRankComic(type, page).collectLatest {
                _rankData.postValue(it)
            }
        }
    }

    fun adapterClick(pos: Int, item: IndexBean) {
        if (item.sort == 5) {
            if (item.data[pos].url.isEmpty()) {
                val mBundle = Bundle()
                mBundle.putInt("tagId", item.data[pos].objId)
                mBundle.putString("title", item.data[pos].subTitle)
//                val intent = Intent(mContext, ComicSpecialDetailActivity::class.java)
//                intent.putExtras(mBundle)
//                mContext.startActivity(intent)
                _toSpecialDetail.value = Event(mBundle)
            } else {
                val mBundle = Bundle()
                mBundle.putString("url", item.data[pos].url)
//                val intent = Intent(mContext, WebActivity::class.java)
//                intent.putExtras(mBundle)
//                mContext.startActivity(intent)
                _toWeb.value = Event(mBundle)
            }
        } else {
            val mBundle = Bundle()
            mBundle.putInt("comicId", item.data[pos].objId)
//            val intent = Intent(mContext, ComicIntroActivity::class.java)
//            intent.putExtras(mBundle)
//            mContext.startActivity(intent)
            _toIntro.value = Event(mBundle)
        }
    }

    fun bannerClick(pos: Int) {
        _homeBanner.value?.data?.let {
            if (it[pos].url.isBlank()) {
                val mBundle = Bundle()
                mBundle.putInt("comicId", it[pos].objId)
                _toIntro.value = Event(mBundle)
            } else {
                val mBundle = Bundle()
                mBundle.putString("url", it[pos].url)
                _toWeb.value = Event(mBundle)
            }
        }
    }
}