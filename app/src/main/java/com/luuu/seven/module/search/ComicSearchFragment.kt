package com.luuu.seven.module.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.*
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicHotSearchAdapter
import com.luuu.seven.adapter.ComicSearchAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.HotSearchBean
import com.luuu.seven.bean.SearchDataBean
import com.luuu.seven.util.*
import com.luuu.seven.widgets.SearchEditText
import kotlinx.android.synthetic.main.fra_comic_search.*

/**
 * Created by lls on 2017/8/9.
 *搜索界面
 */
class ComicSearchFragment : BaseFragment() {

    private val mViewModel: SearchViewModel by viewModels()
    private var mAdapter: ComicSearchAdapter? = null
    private var mHotAdapter: ComicHotSearchAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(requireContext()) }
    private val mFlexLayoutManager: FlexboxLayoutManager by lazy {
        FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
            alignItems = AlignItems.CENTER
            justifyContent = JustifyContent.FLEX_START
        }
    }
    private var mSearchDataBeanList: List<SearchDataBean>? = ArrayList()

    override fun initViews() {

        BarUtils.addStatusBarView(status_bg, requireContext(), color(R.color.colorPrimary))

        mViewModel.getHotSearch()

        mViewModel.searchData.observe(this) {
            if (it.isEmpty()) {
                toast(string(R.string.search_empty))
                return@observe
            }
            updateSearchData(it)
        }

        mViewModel.hotSearchData.observe(this) {
            updateHotSearch(it)
        }

        mViewModel.isEmpty.observe(this) {
            if (it) toast(string(R.string.search_empty))
        }

        tv_cancel.click { nav().navigateUp() }

        back.click {
            nav().navigateUp()
        }

        et_search_view.setQueryTextChangeListener(object : SearchEditText.QueryTextListener {
            override fun onQueryTextSubmit(query: String?) {
                if (query.isNullOrBlank()) {
                    toast(string(R.string.search_input_keyword_first))
                } else {
                    mViewModel.getSearchData(query)
                }
            }

            override fun onQueryTextChange(newText: String?) {
            }

        })

    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_comic_search

    private fun updateSearchData(data: List<SearchDataBean>) {
        tv_hot_search.gone()
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
//            startActivity<ComicIntroActivity>(ComicIntroActivity.COMIC_ID to mSearchDataBeanList!![position].id)
            nav().navigate(
                R.id.action_search_fragment_to_intro_fragment,
                Bundle().apply { putInt("comicId", mSearchDataBeanList!![position].id) })
        }
    }
}
