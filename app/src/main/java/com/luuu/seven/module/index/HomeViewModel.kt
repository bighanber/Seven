package com.luuu.seven.module.index

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.repository.HomeRepository
import com.luuu.seven.util.launch
import com.luuu.seven.util.toast

class HomeViewModel : ViewModel() {

    private val mRepository by lazy { HomeRepository() }

    private val _homeData = MutableLiveData<List<IndexBean>>()
    val homeData: LiveData<List<IndexBean>>
        get() = _homeData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    fun getHomeData() {
        launch<List<IndexBean>> {
            request {
                mRepository.getHomeData()
            }
            onSuccess { result ->
                _homeData.value = result
            }
            onFailed { error, code ->
                toast(error)
            }
        }
    }
}