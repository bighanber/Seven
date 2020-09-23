package com.luuu.seven.module.sort

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicSortListAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicSortListBean
import com.luuu.seven.util.DataLoadType
import kotlinx.android.synthetic.main.fra_tab_layout.*

/**
 * Created by lls on 2017/8/4.
 *
 */
class ComicSortListFragment : BaseFragment() {

    companion object {
        private val SORT_ID = "sortId"

        fun newInstance(type: Int): ComicSortListFragment {
            val fragment = ComicSortListFragment()
            val bundle = Bundle()
            bundle.putInt(SORT_ID, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mSortListBeanList: MutableList<ComicSortListBean>? = null
    private var mPageNum = 0
    private var sortId = 0
    private var mAdapter: ComicSortListAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sortId = it.getInt(SORT_ID)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSortListBeanList = null
    }

    override fun onFragmentVisibleChange(isVisible: Boolean) {
        if (isVisible) {
            mSortListBeanList = ArrayList()
            mPageNum = 0
//            mPresent.getComicSortList(sortId, 0)
        }
    }

    override fun initViews() {
        refresh.setOnRefreshListener {
            mPageNum = 0
//            mPresent.refreshData(sortId)
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_tab_layout


    private fun updateComicSortList(data: List<ComicSortListBean>, type: DataLoadType) {
        when(type) {
            DataLoadType.TYPE_REFRESH_SUCCESS -> {
                mSortListBeanList = data.toMutableList()
                if (mAdapter == null) {
                    initAdapter(data)
                } else {
                    mAdapter!!.setNewData(data)
                }
            }
            DataLoadType.TYPE_LOAD_MORE_SUCCESS -> {
                if (data.isEmpty()) {
                    showToast("没有数据咯")
                    mAdapter!!.loadMoreEnd()
                } else {
                    mAdapter!!.addData(data)
                    mAdapter!!.loadMoreComplete()
                    mSortListBeanList!!.addAll(data)
                }
            }
            else -> {
            }
        }
    }

    private fun judgeRefresh(isRefresh: Boolean) {
        refresh.isEnabled = isRefresh
    }

    private fun initAdapter(sortListBeanList: List<ComicSortListBean>) {
        mAdapter = ComicSortListAdapter(R.layout.item_sort_list_layout, sortListBeanList).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
//                mPresent.loadMoreData(sortId, mPageNum)
            }, recycler)
            setOnItemClickListener { _, _, position ->
//                val mBundle = Bundle()
//                mBundle.putInt("comicId", mSortListBeanList!![position].id)
//                startNewActivity(ComicIntroActivity::class.java, mBundle)
            }
        }
        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter


    }
}