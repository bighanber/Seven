package com.luuu.seven.module.special

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luuu.seven.bean.ComicSpecialBean
import com.luuu.seven.bean.ComicSpecialDetBean
import com.luuu.seven.repository.SpecialRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SpecialViewModel : ViewModel() {
    private val mRepository by lazy { SpecialRepository() }

    private val _specialData = MutableLiveData<List<ComicSpecialBean>>()
    val specialData: LiveData<List<ComicSpecialBean>> = _specialData

    private val _specialDetail = MutableLiveData<ComicSpecialDetBean>()
    val specialDetail: LiveData<ComicSpecialDetBean> = _specialDetail

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun getSpecialComic(page: Int) {
        viewModelScope.launch {
            mRepository.getSpecialComic(page).collectLatest {
                _specialData.value = it
            }
        }
    }

    fun getSpecialComicDetail(id: Int) {
        viewModelScope.launch {
            mRepository.getSpecialComicDetail(id).collectLatest {
                _specialDetail.value = it
            }
        }
    }
}