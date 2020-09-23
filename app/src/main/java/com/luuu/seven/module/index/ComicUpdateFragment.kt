package com.luuu.seven.module.index

import android.os.Bundle
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luuu.seven.R
import com.luuu.seven.adapter.ComicUpdateAdapter
import com.luuu.seven.base.BaseFragment
import com.luuu.seven.bean.ComicUpdateBean
import com.luuu.seven.util.dp2px
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

    private val mViewModel: HomeViewModel by viewModels()
    private var mUpdateBeanList: ArrayList<ComicUpdateBean> = ArrayList()
    private var mPageNum = 0
    private var num = 100
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
        mPageNum = 0
        mViewModel.getComicUpdate(num, 0)
    }

    override fun initViews() {

        recycler.updatePadding(left = dp2px(15), top = paddingTop(requireContext()), right = dp2px(15))

        mViewModel.updateData.observe(viewLifecycleOwner) { data ->
            mUpdateBeanList.addAll(data)

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

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                refresh.isEnabled = mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        })
        refresh.setOnRefreshListener {
            mPageNum = 0
            mViewModel.getComicUpdate(num, mPageNum)
        }
    }

    private fun initAdapter() {
        mAdapter = ComicUpdateAdapter(R.layout.item_comic_layout, mUpdateBeanList).apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener({
                mPageNum++
                mViewModel.getComicUpdate(num, mPageNum)
            }, recycler)
            setOnItemClickListener { _, _, position ->
                val mBundle = Bundle()
                mBundle.putInt("comicId", mUpdateBeanList[position].id)
//                startNewActivity(ComicIntroActivity::class.java, mBundle)
                findNavController().navigate(R.id.action_home_fragment_to_intro_fragment, mBundle)
            }
        }
        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter
        recycler.addItemDecoration(SpaceItemDecoration(mContext!!).setSpace(10))


    }

}