package com.luuu.seven.module.read

import android.graphics.Point
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luuu.seven.bean.ComicReadBean
import com.luuu.seven.bean.ReadHistoryBean
import com.luuu.seven.repository.ReadRepository
import com.luuu.seven.util.Event
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ReadViewModel : ViewModel() {

    private val mRepository by lazy { ReadRepository() }
    private var readHistoryData: ReadHistoryBean? = null

    private val _comicPageData = MutableLiveData<ComicReadBean>()
    val comicPageData: LiveData<ComicReadBean> = _comicPageData

    private val _updateOrInsert = MutableLiveData<Boolean>()
    val updateOrInsert: LiveData<Boolean> = _updateOrInsert

    private val _clickLeft = MutableLiveData<Event<Boolean>>()
    val clickLeft: LiveData<Event<Boolean>> = _clickLeft

    private val _clickRight = MutableLiveData<Event<Boolean>>()
    val clickRight: LiveData<Event<Boolean>> = _clickRight

    private val _clickMid = MutableLiveData<Event<Boolean>>()
    val clickMid: LiveData<Event<Boolean>> = _clickMid

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun getComicReadPage(comicId: Int, chapterId: Int) {
        viewModelScope.launch {
            mRepository.getComicReadPage(comicId, chapterId).collectLatest {
                _comicPageData.value = it
            }
        }
    }

    fun updateOrInsertReadData(data: ReadHistoryBean) {
        viewModelScope.launch {
            mRepository.isReadInChapter(data.comicId).map {
                if (it) {
                    mRepository.updateReadHistory(data)
                } else {
                    mRepository.insertHistory(data)
                }
            }.collectLatest {
                _updateOrInsert.value = true
            }
        }
    }

    fun checkClickPoint(x: Float, y: Float, point: Point) {
        val limitX = point.x / 3.0f
        val limitY = point.y / 3.0f
        when {
            x * point.x < limitX -> {
                _clickLeft.value = Event(true)
            }
            x * point.x > 2 * limitX -> {
                _clickRight.value = Event(true)
            }
            y * point.y < limitY -> {
                //上边
            }
            y * point.y > 2 * limitY -> {
                //下边
            }
            else -> _clickMid.value = Event(true)
        }
    }
}