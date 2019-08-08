package com.luuu.seven.module.index

import androidx.lifecycle.*
import com.luuu.seven.bean.IndexBean
import com.luuu.seven.repository.HomeRepository
import com.luuu.seven.util.launch
import com.luuu.seven.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val mRepository by lazy { HomeRepository() }

    private val _homeData = MutableLiveData<List<IndexBean>>()
    val homeData: LiveData<List<IndexBean>>
        get() = _homeData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    fun getHomeData(showLoading: Boolean) {
//        launch<List<IndexBean>> {
//            request { mRepository.getHomeData() }
//        }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                mRepository.getHomeData()
            }
            _homeData.value = result
        }
//        return mRepository.getHomeData()
//                .compose(ioMain())
//                .compose(handleLoading(showLoading, _dataLoading))
//                .subscribe({
//                    _homeData.value = it
//                }, {
//                    toast(it.message)
//                }, {})
    }
}