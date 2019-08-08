package com.luuu.seven.module.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.*
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicHotSearchAdapter
import com.luuu.seven.adapter.ComicSearchAdapter
import com.luuu.seven.base.BaseActivity
import com.luuu.seven.bean.HotSearchBean
import com.luuu.seven.bean.SearchDataBean
import com.luuu.seven.module.intro.ComicIntroActivity
import kotlinx.android.synthetic.main.activity_comic_search.*

/**
 * Created by lls on 2017/8/9.
 *搜索界面
 */
class ComicSearchActivity : BaseActivity(), ComicSearchContract.View {

    private var mAdapter: ComicSearchAdapter? = null
    private var mHotAdapter: ComicHotSearchAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(this) }
    private lateinit var mFlexboxLayoutManager: FlexboxLayoutManager
    private var mSearchDataBeanList: List<SearchDataBean>? = ArrayList()
    private val mPresenter by lazy { ComicSearchPresenter(this) }

    override fun initViews() {
        mPresenter.getHotSearch()
        tv_cancel.setOnClickListener { onBackPressed() }
        et_search_view.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyWords = et_search_view.text.toString().trim()
                if (keyWords.isEmpty()) {
                    showToast(recycler_search,"请输入搜索内容")
                } else {
                    mPresenter.getSearchData(keyWords)
                }
            }
            false
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.unsubscribe()
//        mSearchDataBeanList = null
    }

    override fun getIntentExtras(extras: Bundle?) {
    }

    override fun getContentViewLayoutID(): Int = R.layout.activity_comic_search

    override fun showLoading(isLoading: Boolean) {
    }

    override fun showError(isError: Boolean) {
    }

    override fun showEmpty(isEmpty: Boolean) {
        if (isEmpty) showToast(recycler_search, "查不到数据哦")
    }

    override fun updateSearchData(dataBeen: List<SearchDataBean>) {
        tv_hot_search.visibility = View.GONE
        mSearchDataBeanList = dataBeen
        if (mAdapter == null) {
            initAdapter(dataBeen)
        } else {
            mAdapter!!.setNewData(dataBeen)
        }
    }

    override fun updateHotSearch(data: List<HotSearchBean>) {
        mFlexboxLayoutManager = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            alignItems = AlignItems.CENTER
            justifyContent = JustifyContent.FLEX_START
        }

        mHotAdapter = ComicHotSearchAdapter(R.layout.item_hot_search_layout, data.take(20))
        recycler_search.layoutManager = mFlexboxLayoutManager
        recycler_search.adapter = mHotAdapter

        mHotAdapter!!.setOnItemClickListener { _, _, position ->
            mPresenter.getSearchData(data[position].name)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et_search_view.windowToken, 0)
        }
    }

    private fun initAdapter(searchDataBeanList: List<SearchDataBean>) {
        mAdapter = ComicSearchAdapter(R.layout.item_search_layout, searchDataBeanList)
        recycler_search.layoutManager = mLayoutManager
        recycler_search.adapter = mAdapter
        mAdapter!!.setOnItemClickListener { _, _, position ->
            val mBundle = Bundle()
            mBundle.putInt("comicId", mSearchDataBeanList!![position].id)
            startNewActivity(ComicIntroActivity::class.java, mBundle)
        }
    }
}
