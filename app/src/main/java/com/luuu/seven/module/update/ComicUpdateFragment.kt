package com.luuu.seven.module.update

import android.os.Bundle
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicUpdateAdapter
import com.luuu.seven.adapter.DiffUpdateCallback
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.util.dp2px
import com.luuu.seven.util.nav
import com.luuu.seven.util.observeEvent
import com.luuu.seven.util.paddingTop
import com.luuu.seven.widgets.SpaceItemDecoration
import kotlinx.android.synthetic.main.fra_tab_layout.*


/**
 *     author : dell
 *     e-mail :
 *     time   : 2017/08/03
 *     desc   :
 *     version:
 */
class ComicUpdateFragment : BaseFragment() {

    private val mViewModel: UpdateViewModel by viewModels()
    private var mUpdateBeanList: ArrayList<ComicUpdateBean> = ArrayList()
    private var mAdapter: ComicUpdateAdapter? = null
    private val mLayoutManager by lazy { GridLayoutManager(mContext, 3) }

    companion object {
        fun newInstance(): ComicUpdateFragment {
            return ComicUpdateFragment()
        }
    }

    override fun getContentViewLayoutID(): Int = R.layout.fra_tab_layout

    override fun onResume() {
        super.onResume()
        mViewModel.getComicUpdate()
    }

    override fun initViews() {

        recycler.updatePadding(left = dp2px(15), top = paddingTop(requireContext()), right = dp2px(15))

        mViewModel.updateList.observe(viewLifecycleOwner) { data ->

            mUpdateBeanList.clear()
            mUpdateBeanList.addAll(data)
            mAdapter?.setNewDiffData(DiffUpdateCallback(data)) ?: initAdapter()
        }

        mViewModel.loadMore.observe(viewLifecycleOwner) {
            mUpdateBeanList.addAll(it)
            mAdapter?.let { adapter ->
                adapter.loadMoreComplete()
                adapter.setNewDiffData(DiffUpdateCallback(mUpdateBeanList))
            }
        }

        mViewModel.swipeRefreshing.observeEvent(viewLifecycleOwner) {
            if (refresh.isRefreshing) {
                refresh.isRefreshing = it
            }
        }

        mViewModel.loadMoreEnd.observeEvent(viewLifecycleOwner) {
            if (it) {
                mAdapter?.loadMoreEnd()
            }
        }

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                refresh.isEnabled = mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        })

        refresh.setOnRefreshListener {
            mViewModel.updateRefresh()
        }
    }

    private fun initAdapter() {
        mAdapter = ComicUpdateAdapter(R.layout.item_comic_layout, mUpdateBeanList).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mViewModel.updateLoadNextPage()
            }, recycler)
            setOnItemClickListener { _, _, position ->
                val mBundle = Bundle()
                mBundle.putInt("comicId", mUpdateBeanList[position].id)
                nav().navigate(R.id.action_home_fragment_to_intro_fragment, mBundle)
            }
        }
        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter
        recycler.addItemDecoration(SpaceItemDecoration(mContext!!).setSpace(10))
    }

}