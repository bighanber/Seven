package com.luuu.seven.module.intro

import android.os.Bundle
import androidx.lifecycle.*
import com.luuu.seven.R
import com.luuu.seven.bean.*
import com.luuu.seven.repository.IntroRepository
import com.luuu.seven.util.Event
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.ArrayList

class IntroViewModel : ViewModel() {

    private val mRepository by lazy { IntroRepository() }

    private val _comicIntroData = MutableLiveData<ComicIntroBean>()
    val comicIntroData: LiveData<ComicIntroBean> = _comicIntroData

    private val _updateFavorite = MutableLiveData<Boolean>()
    val updateFavorite: LiveData<Boolean> = _updateFavorite

    private val _readHistory = MutableLiveData<List<ReadHistoryBean>>()
    val readHistory: LiveData<List<ReadHistoryBean>> = _readHistory

    private val _comicRelatedData = MutableLiveData<ComicRelatedInfoBean>()
    val comicRelatedData: LiveData<ComicRelatedInfoBean> = _comicRelatedData

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    val comicIntroFilter: LiveData<List<ChapterDataBean>> = _comicIntroData.map {
        it.chapters[0].data.take(8)
    }

    private val moreThanLimit = _comicIntroData.map {
        it.chapters[0].data.size > 8
    }

    private val _bottomSheetExpand = MutableLiveData<Event<Boolean>>()
    val bottomSheetExpand: LiveData<Event<Boolean>> = _bottomSheetExpand

    private val _toReadPage = MutableLiveData<Event<Bundle>>()
    val toReadPage: LiveData<Event<Bundle>> = _toReadPage

    fun getComicIntro(comicId: Int) {
        viewModelScope.launch {
            mRepository.getComicIntro(comicId).collectLatest {
                _comicIntroData.value = it
            }
        }
    }

    fun getComicRelated(comicId: Int) {
        viewModelScope.launch {
            mRepository.getComicRelated(comicId).collectLatest {
                _comicRelatedData.value = it
            }
        }
    }

    fun getReadHistory(comicId: Int) {
        viewModelScope.launch {
            mRepository.getReadHistory(comicId).collectLatest {
                _readHistory.value = it
            }
        }
    }

    fun favoriteComic(comicId: Int, comicTitle: String, comicAuthors: String, comicCover: String, time: Long) {
        viewModelScope.launch {
            mRepository.favoriteComic(CollectBean(comicId, comicTitle, comicAuthors, comicCover, time))
            _updateFavorite.value = true
        }
    }

    fun isFavorite(comicId: Int) {
        viewModelScope.launch {
            mRepository.isFavorite(comicId).collectLatest {
                _updateFavorite.value = it
            }
        }
    }

    fun unFavoriteComic(comicId: Int) {
        viewModelScope.launch {
            mRepository.unFavoriteComic(comicId)
            _updateFavorite.value = false
        }
    }

    fun toRead(pos: Int, hisPos: Int, id: Int) {
        if (moreThanLimit.value == true && pos == (comicIntroFilter.value?.size ?: 1) - 1) {
            _bottomSheetExpand.value = Event(true)
        } else {

            val mChapterList: ArrayList<ChapterDataBean> = ArrayList()
            comicIntroData.value?.let {
                val mBundle = Bundle()
                mChapterList.addAll(it.chapters[0].data)
                mBundle.run {
                    putInt("comicId", id)
                    putParcelableArrayList("comicChapter", mChapterList)
                    putInt("comicPosition", pos)
                    putString("comicTagName", mChapterList[pos].chapterTitle)
                    putString("comicCover", it.cover)
                    putString("comicTitle", it.title)
                    putInt(
                        "historyPosition",
                        if (pos + 1 == hisPos) hisPos else 0
                    )
                }
                _toReadPage.value = Event(mBundle)
            }
        }
    }

    fun getChapterInfo(pos: Int, item: ChapterDataBean?): ChapterInfo {
        val historyRead = _readHistory.value?.getOrNull(0)
        val name = if (moreThanLimit.value == true && pos == (comicIntroFilter.value?.size ?: 1) - 1) {
            "..."
        } else {
            item?.chapterTitle ?: ""
        }
        return if (historyRead != null && item?.isReadHistory(historyRead.chapterId) == true) {
            ChapterInfo(R.drawable.chapter_read_backgroud, R.color.white, name, pos, historyRead.browsePosition.coerceAtLeast(1) )
        } else {
            ChapterInfo(R.drawable.chapter_backgroud, R.color.content,  name, -1, -1)
        }
    }
}

data class ChapterInfo(
    val background: Int,
    val colorRes: Int,
    val name: String,
    val historyChapterPos: Int,
    val historyBrowsePos: Int
)