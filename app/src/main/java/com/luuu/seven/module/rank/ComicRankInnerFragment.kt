package com.luuu.seven.module.rank

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicRankAdapter
import com.luuu.seven.adapter.DiffRankCallback
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.HotComicBean
import com.luuu.seven.util.autoCleared
import com.luuu.seven.util.nav
import com.luuu.seven.util.observeEvent
import com.luuu.seven.widgets.RankItemDecoration
import kotlinx.android.synthetic.main.fra_tab_layout.*

/**
 * Created by lls on 2017/8/4.
 * 排行二级fragment
 */
class ComicRankInnerFragment : BaseFragment() {

    private val mViewModel: RankViewModel by viewModels()
    private var mRankList by autoCleared<ArrayList<HotComicBean>>()
//    private var mHotComicBeanList by autoCleared<MutableList<HotComicBean>>()
//    private var mHotComicTopList by autoCleared<MutableList<HotComicBean>>()
    private val num by lazy {
        arguments?.getInt(COMIC_TYPE)
    }
    private var mAdapter: ComicRankAdapter? = null
    private val mLayoutManager by lazy { LinearLayoutManager(mContext) }

    companion object {
        const val COMIC_TYPE = "type"

        fun newInstance(type: Int): ComicRankInnerFragment {
            val fragment = ComicRankInnerFragment()
            val bundle = Bundle()
            bundle.putInt(COMIC_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onResume() {
        super.onResume()
        mRankList = ArrayList()
//        mHotComicBeanList = ArrayList()
//        mHotComicTopList = ArrayList()
        mViewModel.getRankComic(num ?: 0)
    }

    override fun initViews() {
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                refresh.isEnabled = mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        })

        refresh.setOnRefreshListener {
            mViewModel.rankRefresh(num ?: 0)
        }

        mViewModel.rankList.observe(viewLifecycleOwner) { data ->

            mRankList.clear()
            mRankList.addAll(data)

            mAdapter?.setNewDiffData(DiffRankCallback(data)) ?: initAdapter(data)
        }

        mViewModel.loadMore.observe(viewLifecycleOwner) {
            mRankList.addAll(it)
            mAdapter?.let { adapter ->
                adapter.loadMoreComplete()
                adapter.setNewDiffData(DiffRankCallback(mRankList))
            }
        }

        mViewModel.swipeRefreshing.observeEvent(viewLifecycleOwner) {
            if (refresh.isRefreshing) {
                refresh.isRefreshing = it
            }
        }

        mViewModel.loadMoreEnd.observeEvent(viewLifecycleOwner) { end ->
            if (end) {
                mAdapter?.loadMoreEnd()
            }
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_tab_layout

    private fun initAdapter(list: List<HotComicBean>) {
        mAdapter = ComicRankAdapter(R.layout.item_rank_layout, list).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mViewModel.rankLoadNextPage(num ?: 0)
            }, recycler)
            setOnItemClickListener { _, _, position ->
                val mBundle = Bundle()
                mBundle.putInt("comicId", this.data[position].comicId)
                nav().navigate(R.id.action_home_fragment_to_intro_fragment, mBundle)
            }
        }

        recycler.layoutManager = mLayoutManager
        recycler.addItemDecoration(RankItemDecoration(requireContext()))
        recycler.adapter = mAdapter


    }
}