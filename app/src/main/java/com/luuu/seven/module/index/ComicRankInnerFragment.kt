package com.luuu.seven.module.index

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicRankAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.HotComicBean
import com.luuu.seven.util.nav
import com.luuu.seven.widgets.RankItemDecoration
import kotlinx.android.synthetic.main.fra_tab_layout.*

/**
 * Created by lls on 2017/8/4.
 * 排行二级fragment
 */
class ComicRankInnerFragment : BaseFragment() {

    private val mViewModel: HomeViewModel by viewModels()
    private var mRankBeanList: ArrayList<HotComicBean> = ArrayList()
    private var mHotComicBeanList: MutableList<HotComicBean>? = null
    private var mHotComicTopList: MutableList<HotComicBean>? = null
    private var mPageNum = 0
    private var pos = 0
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
        mHotComicBeanList = ArrayList()
        mHotComicTopList = ArrayList()
        mPageNum = 0
        mViewModel.getRankComic(num ?: 0, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHotComicTopList = null
        mHotComicBeanList = null
    }

    override fun initViews() {
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                refresh.isEnabled = mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        })
        refresh.setOnRefreshListener {
            mPageNum = 0
            mViewModel.getRankComic(num ?: 0, mPageNum)
        }


        mViewModel.rankData.observe(viewLifecycleOwner) { data ->
            mRankBeanList.addAll(data)

            mAdapter?.let { adapter ->
                adapter.loadMoreComplete()

                if (refresh.isRefreshing) {
                    refresh.isRefreshing = false
                } else {
                    if (data.isEmpty()) {
                        adapter.loadMoreEnd()
                    } else {
                        adapter.notifyDataSetChanged()
                    }
                }
            } ?: initAdapter()
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_tab_layout

    private fun initAdapter() {
        mAdapter = ComicRankAdapter(R.layout.item_rank_layout, mRankBeanList).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                mViewModel.getRankComic(num ?: 0, mPageNum)
            }, recycler)
            setOnItemClickListener { _, _, position ->
                val mBundle = Bundle()
                mBundle.putInt("comicId", mRankBeanList[position].comicId)
//                startNewActivity(ComicIntroActivity::class.java, mBundle)
                nav().navigate(R.id.action_home_fragment_to_intro_fragment, mBundle)
            }
        }

        recycler.layoutManager = mLayoutManager
        recycler.addItemDecoration(RankItemDecoration(mContext!!))
        recycler.adapter = mAdapter


    }
}