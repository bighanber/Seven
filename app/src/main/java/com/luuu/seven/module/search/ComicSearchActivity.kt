package com.luuu.seven.module.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.*
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicHotSearchAdapter
import com.luuu.seven.adapter.ComicSearchAdapter
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.bean.HotSearchBean
import com.luuu.seven.bean.SearchDataBean
import com.luuu.seven.module.intro.ComicIntroActivity
import com.luuu.seven.util.*
import kotlinx.android.synthetic.main.activity_comic_search.*

/**
 * Created by lls on 2017/8/9.
 *搜索界面
 */
class ComicSearchActivity : BaseActivity() {

    private lateinit var mViewModel: SearchViewModel
    private var mAdapter: ComicSearchAdapter? = null
    private var mHotAdapter: ComicHotSearchAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(this) }
    private val mFlexLayoutManager: FlexboxLayoutManager by lazy {
        FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            alignItems = AlignItems.CENTER
            justifyContent = JustifyContent.FLEX_START
        }
    }
    private var mSearchDataBeanList: List<SearchDataBean>? = ArrayList()

    override fun initViews() {

        mViewModel = obtainViewModel<SearchViewModel>().apply {
            getHotSearch()

            searchData.observe(this@ComicSearchActivity, Observer {
                if (it.isEmpty()) {
                    toast(string(R.string.search_empty))
                    return@Observer
                }
                updateSearchData(it)
            })

            hotSearchData.observe(this@ComicSearchActivity, Observer {
                updateHotSearch(it)
            })

            isEmpty.observe(this@ComicSearchActivity, Observer {
                if (it) toast(string(R.string.search_empty))
            })
        }

        tv_cancel.setOnClickListener { finish() }

        et_search_view.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyWords = et_search_view.values()
                if (keyWords.isEmpty()) {
                    toast(string(R.string.search_input_keyword_first))
                } else {
                    mViewModel.getSearchData(keyWords)
                }
            }
            false
        }

    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_comic_search

    private fun updateSearchData(data: List<SearchDataBean>) {
        tv_hot_search.setGone()
        mSearchDataBeanList = data
        mAdapter?.setNewData(data) ?: initAdapter(data)
    }

    private fun updateHotSearch(data: List<HotSearchBean>) {

        mHotAdapter = ComicHotSearchAdapter(R.layout.item_hot_search_layout, data.take(20))
        recycler_search.apply {
            layoutManager = mFlexLayoutManager
            adapter = mHotAdapter
        }

        mHotAdapter?.setOnItemClickListener { _, _, position ->
            mViewModel.getSearchData(data[position].name)
            et_search_view.dismissKeyboard()
        }
    }

    private fun initAdapter(searchDataBeanList: List<SearchDataBean>) {
        mAdapter = ComicSearchAdapter(R.layout.item_search_layout, searchDataBeanList)
        recycler_search.layoutManager = mLayoutManager
        recycler_search.adapter = mAdapter
        mAdapter?.setOnItemClickListener { _, _, position ->
            startActivity<ComicIntroActivity>(ComicIntroActivity.COMIC_ID to mSearchDataBeanList!![position].id)
        }
    }
}
