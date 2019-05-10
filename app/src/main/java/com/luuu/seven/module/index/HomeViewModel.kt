package com.luuu.seven.module.index

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.repository.HomeRepository
import com.luuu.seven.util.handleLoading
import com.luuu.seven.util.ioMain
import com.luuu.seven.util.toast
import io.reactivex.disposables.Disposable

class HomeViewModel : ViewModel() {

    private val mRepository by lazy { HomeRepository() }

    private val _homeData = MutableLiveData<List<IndexBean>>()
    val homeData: LiveData<List<IndexBean>>
        get() = _homeData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    fun getHomeData(showLoading: Boolean): Disposable {
        return mRepository.getHomeData()
                .compose(ioMain())
                .compose(handleLoading(showLoading, _dataLoading))
                .subscribe({
                    _homeData.value = it
                }, {
                    toast(it.message)
                }, {})
    }
}